import { Injectable } from "@angular/core";
import { Subject } from "rxjs";
import {
  AbstractSocketService,
  COLLABORATIVE_DRAWING_NAMESPACE,
  CONFIRM_DRAWING_EVENT,
  CONFIRM_ERASE_EVENT,
  CONFIRM_SELECTION_EVENT,
  DELETE_SELECTION_EVENT,
  IN_PROGRESS_DRAWING_EVENT,
  START_SELECTION_EVENT,
  TRANSFORM_SELECTION_EVENT,
  UPDATE_DRAWING_EVENT,
  FETCH_DRIVING_EVENT,
  UPDATE_DRAWING_NOTIFICATION,
  ONE_USER_RESPONSE,
} from "src/app/shared";
import { Selection } from "../../../tools/selection-tool/selection.model";
import { SocketTool } from "../../../tools/socket-tool";
import { SynchronisationService } from "../../synchronisation.service";
import { DrawingHttpClientService } from "src/app/modules/backend-communication";
import { DrawingService } from "src/app/modules/workspace";

@Injectable({
  providedIn: "root",
})
export class DrawingSocketService extends AbstractSocketService {
  roomName: string;
  drawingSubject: Subject<boolean> = new Subject();

  constructor(
    private synchronisationService: SynchronisationService,
    private drawingHttpClientService: DrawingHttpClientService,
    private drawingService: DrawingService
  ) {
    super();
    this.init();
  }

  connect(): void {
    super.connect();
  }

  joinRoom(roomName: string) {
    this.roomName = roomName;
    super.joinRoom(roomName);
  }

  protected init(): void {
    super.init(COLLABORATIVE_DRAWING_NAMESPACE);
  }

  protected setSocketListens(): void {
    this.listenInProgressDrawingCommand();
    this.listenConfirmDrawingCommand();
    this.listenConfirmEraseCommand();
    this.listenStartSelectionCommand();
    this.listenConfirmSelectionCommand();
    this.listenTransformSelectionCommand();
    this.listenDeleteSelectionCommand();
    this.listenUpdateDrawingRequest();
    this.listenFetchDrawingNotification();
  }

  async sendGetUpdateDrawingRequest(): Promise<void> {
    return await new Promise((resolve) => {
      this.emitWithCallback(UPDATE_DRAWING_EVENT, this.roomName, (response) => {
        if (response.status == ONE_USER_RESPONSE) {
          this.drawingHttpClientService
            .getDrawing(this.drawingService.drawingId)
            .subscribe((response) => {
              this.drawingService.openSvgFromDataUri(response.dataUri);
            });
        }
      });
    });
  }

  sendInProgressDrawingCommand(drawingCommand: any, type: string): void {
    let socketToolCommand: SocketTool = {
      type: type,
      roomName: this.roomName,
      drawingCommand: drawingCommand,
    };

    this.emit(IN_PROGRESS_DRAWING_EVENT, socketToolCommand);
  }

  sendConfirmDrawingCommand(drawingCommand: any, type: string): void {
    let socketToolCommand: SocketTool = {
      type: type,
      roomName: this.roomName,
      drawingCommand: drawingCommand,
    };

    this.emit(CONFIRM_DRAWING_EVENT, socketToolCommand);
  }

  sendConfirmEraseCommand(itemsToDeleteIds: String[], type: string): void {
    let eraseCommand = {
      type: type,
      roomName: this.roomName,
      itemsToDeleteIds: itemsToDeleteIds,
    };
    this.emit(CONFIRM_ERASE_EVENT, eraseCommand);
  }

  sendStartSelectionCommand(
    selectionStartCommand: Selection,
    type: string
  ): void {
    let selectionCommand: SocketTool = {
      type: type,
      roomName: this.roomName,
      drawingCommand: selectionStartCommand,
    };
    this.emit(START_SELECTION_EVENT, selectionCommand);
  }

  sendConfirmSelectionCommand(
    confirmSelectionCommand: Selection,
    type: string
  ): void {
    let selectionCommand: SocketTool = {
      type: type,
      roomName: this.roomName,
      drawingCommand: confirmSelectionCommand,
    };
    this.emit(CONFIRM_SELECTION_EVENT, selectionCommand);
  }

  sendTransformSelectionCommand(
    transformSelectionCommand: any,
    type: string
  ): void {
    let transformCommand: SocketTool = {
      type: type,
      roomName: this.roomName,
      drawingCommand: transformSelectionCommand,
    };
    this.emit(TRANSFORM_SELECTION_EVENT, transformCommand);
  }

  sendDeleteSelectionCommand(deleteSelectionCommand: any, type: string): void {
    let deleteCommand: SocketTool = {
      type: type,
      roomName: this.roomName,
      drawingCommand: deleteSelectionCommand,
    };

    this.emit(DELETE_SELECTION_EVENT, deleteCommand);
  }

  private listenConfirmEraseCommand(): void {
    this.namespaceSocket.on(CONFIRM_ERASE_EVENT, (eraseCommand: any) => {
      this.synchronisationService.erase(eraseCommand);
    });
  }

  private listenStartSelectionCommand(): void {
    this.namespaceSocket.on(
      START_SELECTION_EVENT,
      (selectionCommand: SocketTool) => {
        this.synchronisationService.startSelection(selectionCommand);
      }
    );
  }

  private listenConfirmSelectionCommand(): void {
    this.namespaceSocket.on(
      CONFIRM_SELECTION_EVENT,
      (confirmSelectionCommand: any) => {
        this.synchronisationService.confirmSelection(confirmSelectionCommand);
      }
    );
  }

  private listenTransformSelectionCommand(): void {
    this.namespaceSocket.on(
      TRANSFORM_SELECTION_EVENT,
      (transformSelectionCommand: any) => {
        this.synchronisationService.transformSelection(
          transformSelectionCommand
        );
      }
    );
  }

  private listenDeleteSelectionCommand(): void {
    this.namespaceSocket.on(
      DELETE_SELECTION_EVENT,
      (deleteSelectionCommand: any) => {
        this.synchronisationService.deleteSelection(deleteSelectionCommand);
      }
    );
  }

  private listenInProgressDrawingCommand(): void {
    this.namespaceSocket.on(
      IN_PROGRESS_DRAWING_EVENT,
      (drawingCommand: SocketTool) => {
        this.synchronisationService.draw(drawingCommand);
      }
    );
  }

  private listenConfirmDrawingCommand(): void {
    this.namespaceSocket.on(
      CONFIRM_DRAWING_EVENT,
      (drawingCommand: SocketTool) => {
        this.synchronisationService.removeFromPreview(
          drawingCommand.drawingCommand.id
        );
      }
    );
  }

  private listenUpdateDrawingRequest(): void {
    this.namespaceSocket.on(UPDATE_DRAWING_EVENT, (request: any) => {
      this.drawingService.saveDrawing().then(() => {
        this.sendDrawingUpdatedNotification(request.newUserId);
      });
    });
  }

  private listenFetchDrawingNotification(): void {
    this.namespaceSocket.on(FETCH_DRIVING_EVENT, () => {
      this.drawingHttpClientService
        .getDrawing(this.drawingService.drawingId)
        .subscribe((response) => {
          this.drawingService.openSvgFromDataUri(response.dataUri);
        });
    });
  }

  private sendDrawingUpdatedNotification(clientSocketId: string): void {
    this.namespaceSocket.emit(UPDATE_DRAWING_NOTIFICATION, clientSocketId);
  }
}
