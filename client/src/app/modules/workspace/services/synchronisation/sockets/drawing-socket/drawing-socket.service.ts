import { Injectable } from "@angular/core";
import {
  AbstractSocketService,
  COLLABORATIVE_DRAWING_NAMESPACE,
  CONFIRM_DRAWING_EVENT,
  IN_PROGRESS_DRAWING_EVENT,
  CONFIRM_ERASE_EVENT,
  CONFIRM_SELECTION_EVENT,
  START_SELECTION_EVENT,
} from "src/app/shared";
import { Selection } from "../../../tools/selection-tool/selection.model";
import { SocketTool } from "../../../tools/socket-tool";
import { SynchronisationService } from "../../synchronisation.service";

@Injectable({
  providedIn: "root",
})
export class DrawingSocketService extends AbstractSocketService {
  roomName: string;

  constructor(private synchronisationService: SynchronisationService) {
    super();
    this.init();
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

  sendConfirmEraseCommand(itemsToDeleteIds: String[], type:string): void {
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
        this.synchronisationService.removeFromPreview(drawingCommand.drawingCommand.id);
      }
    );
  }
}
