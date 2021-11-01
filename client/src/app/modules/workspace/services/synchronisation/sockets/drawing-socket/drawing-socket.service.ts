import { Injectable } from "@angular/core";
import {
  AbstractSocketService,
  COLLABORATIVE_DRAWING_NAMESPACE,
  DRAW_EVENT,
} from "src/app/shared";
import { SocketTool } from "../../../tools/socket-tool";

@Injectable({
  providedIn: "root",
})
export class DrawingSocketService extends AbstractSocketService {
  roomName: string;

  constructor() {
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
    this.listenDrawingCommand();
  }

  sendDrawingCommand(drawingCommand: any, type: string): void {
    let socketToolCommand: SocketTool = {
      type: type,
      roomName: this.roomName,
      drawingCommand: drawingCommand
    }

    this.emit(DRAW_EVENT, socketToolCommand);
  }

  private listenDrawingCommand(): void {
    this.namespaceSocket.on(DRAW_EVENT, (drawingCommand: any) => {
      console.log(drawingCommand);
    });
  }
}
