import { MatDialog } from "@angular/material/dialog";
import { NewDrawingComponent } from "../../new-drawing";
import { Component, AfterViewInit } from "@angular/core";
import { DrawingHttpClientService } from "../../backend-communication";
import { Router } from "@angular/router";

@Component({
  selector: "app-gallery",
  templateUrl: "./gallery.component.html",
  styleUrls: ["./gallery.component.scss"],
})
  
export class GalleryComponent implements AfterViewInit {
  drawings:Set<any> = new Set(); // TODO: Change for drawing return object
  constructor(
    private router:Router, 
    private dialog:MatDialog,
    private drawingHttpClient:DrawingHttpClientService
  ) {}

  ngAfterViewInit(): void {
    this.drawingHttpClient.getDrawings().subscribe(
      (response) => {
        console.log(response);
        for(let drawing of response){
          this.drawings.add(drawing._id);
        }
      }
    );
    
  }

  createNewDrawing() {
    this.dialog.open(NewDrawingComponent, {});
  }

  goToDrawing(drawingId:string) {
    this.router.navigate([`/drawings/${drawingId}`]);
  }
}
