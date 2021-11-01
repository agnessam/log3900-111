import { Component, AfterViewInit, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { DrawingService } from "../../workspace";
import { DrawingHttpClientService } from "../../backend-communication";
import { DrawingSocketService } from "../../workspace/services/sockets/drawing-socket/drawing-socket.service";
@Component({
  selector: "app-drawing",
  templateUrl: "./drawing.component.html",
  styleUrls: ["./drawing.component.scss"],
})
export class DrawingComponent implements OnInit, AfterViewInit {
  drawingId: string;
  constructor(
    private route: ActivatedRoute,
    private drawingService: DrawingService,
    private drawingHttpClientService: DrawingHttpClientService,
    private drawingSocketService: DrawingSocketService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.drawingId = params["id"];
      this.drawingSocketService.connect();
      this.drawingSocketService.joinRoom(params["id"]);
    });
  }

  ngAfterViewInit(): void {
    this.drawingHttpClientService
      .getDrawing(this.drawingId)
      .subscribe((response) => {
        this.drawingService.openSvgFromDataUri(response.dataUri);
      });
  }

  ngOnDestroy(): void {
    this.drawingSocketService.disconnect();
    this.drawingSocketService.leaveRoom(this.drawingId);
  }
}
