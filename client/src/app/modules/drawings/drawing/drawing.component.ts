import { Component, AfterViewInit, OnInit } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { environment } from "src/environments/environment";
import { ActivatedRoute } from '@angular/router';
import { DrawingService } from '../../workspace';
@Component({
  selector: 'app-drawing',
  templateUrl: './drawing.component.html',
  styleUrls: ['./drawing.component.scss']
})
export class DrawingComponent implements OnInit, AfterViewInit {
  drawingId:string;
  constructor(
    private route:ActivatedRoute, 
    private httpClient:HttpClient, 
    private drawingService:DrawingService
  ) {}

  ngOnInit(): void{
    this.route.params.subscribe((params) => {
      this.drawingId = params["id"];
    });
  }

  ngAfterViewInit(): void{
    this.httpClient.get<any>(`${environment.serverURL}/drawings/${this.drawingId}`).subscribe((response) => {
      this.drawingService.openSvgFromDataUri(response.dataUri);
    });
  }

}
