import { Component, OnInit } from "@angular/core";
import { DrawingSocketService } from "../../workspace/services/sockets/drawing-socket/drawing-socket.service";

@Component({
  selector: "app-drawing",
  templateUrl: "./drawing.component.html",
  styleUrls: ["./drawing.component.scss"],
})
export class DrawingComponent implements OnInit {
  drawingId: string = "491";

  constructor(private drawingSocketService: DrawingSocketService) {}

  ngOnInit(): void {
    this.drawingSocketService.connect();
    this.drawingSocketService.joinRoom(this.drawingId);
  }

  ngOnDestroy(): void {
    this.drawingSocketService.leaveRoom(this.drawingId);
  }
}
