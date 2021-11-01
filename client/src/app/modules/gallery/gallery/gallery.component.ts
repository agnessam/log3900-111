import { MatDialog } from "@angular/material/dialog";
import { NewDrawingComponent } from "../../new-drawing";
import { Component, AfterViewInit } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { environment } from "src/environments/environment";
import { Router } from "@angular/router";

@Component({
  selector: "app-gallery",
  templateUrl: "./gallery.component.html",
  styleUrls: ["./gallery.component.scss"],
})
  
export class GalleryComponent implements AfterViewInit {
  drawings:Set<any> = new Set(); // TODO: Change for drawing return object
  constructor(
    private httpClient: HttpClient, 
    private router:Router, 
    private dialog:MatDialog
  ) {}

  ngAfterViewInit(): void {
    const getDrawingsURL = environment.serverURL + "/drawings/"
    this.httpClient.get<any>(getDrawingsURL).subscribe(
      (response) => {
        for(let drawing of response){
          console.log(drawing._id);
          this.drawings.add(drawing._id);
        }
        console.log(response);
      }
    );
  }

  createNewDrawing() {
    this.dialog.open(NewDrawingComponent, {});
  }

  goToDrawing(drawingId:string) {
    console.log("hello?");
    console.log(drawingId);
    this.router.navigate([`/drawings/${drawingId}`]);
  }
}
