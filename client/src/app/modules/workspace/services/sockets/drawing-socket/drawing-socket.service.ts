import { Injectable } from "@angular/core";
import {
  AbstractSocketService,
  COLLABORATIVE_DRAWING_NAMESPACE,
  DRAW_EVENT,
} from "src/app/shared";

@Injectable({
  providedIn: "root",
})
export class DrawingSocketService extends AbstractSocketService {
  constructor() {
    super();
    this.init();
  }

  protected init(): void {
    super.init(COLLABORATIVE_DRAWING_NAMESPACE);
  }

  protected setSocketListens(): void {
    this.listenDrawingCommand();
  }

  sendDrawingCommand(drawingCommand: any): void {
    this.emit(DRAW_EVENT, drawingCommand);
  }

  private listenDrawingCommand(): void {
    this.namespaceSocket.on(DRAW_EVENT, (drawingCommand: any) => {
      console.log(drawingCommand);
    });
  }
}
