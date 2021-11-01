import { Component, AfterViewInit, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DrawingService } from '../../workspace';
import { DrawingHttpClientService } from '../../backend-communication';
@Component({
  selector: 'app-drawing',
  templateUrl: './drawing.component.html',
  styleUrls: ['./drawing.component.scss']
})
export class DrawingComponent implements OnInit, AfterViewInit {
  drawingId:string;
  constructor(
    private route:ActivatedRoute, 
    private drawingService:DrawingService,
    private drawingHttpClientService: DrawingHttpClientService
  ) {}

  ngOnInit(): void{
    this.route.params.subscribe((params) => {
      this.drawingId = params["id"];
    });
  }

  ngAfterViewInit(): void{
    this.drawingHttpClientService.getDrawing(this.drawingId).subscribe((response) => {
      this.drawingService.openSvgFromDataUri(response.dataUri);
    });
  }
}
