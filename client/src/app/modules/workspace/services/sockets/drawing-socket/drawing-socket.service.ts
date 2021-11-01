import { Injectable } from "@angular/core";
import { AbstractSocketService } from "src/app/shared";
import { Subject } from "rxjs";

const COLLABORATIVE_DRAWING_NAMESPACE = "drawing";
const DRAW_EVENT_NAME = "draw";

@Injectable({
  providedIn: "root",
})
export class DrawingSocketService extends AbstractSocketService {
  public socketId: string;
  public drawingCommandSubject: Subject<any>;

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
    this.emit(DRAW_EVENT_NAME, drawingCommand);
  }

  private listenDrawingCommand(): void {
    this.namespaceSocket.on(DRAW_EVENT_NAME, (drawingCommand: any) => {
      console.log(drawingCommand);
      this.drawingCommandSubject.next(drawingCommand);
    });
  }
}
