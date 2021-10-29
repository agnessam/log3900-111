import { Component, OnInit } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
import { NewDrawingComponent } from "../../new-drawing";

@Component({
  selector: "app-gallery",
  templateUrl: "./gallery.component.html",
  styleUrls: ["./gallery.component.scss"],
})
export class GalleryComponent implements OnInit {
  constructor(private dialog: MatDialog) {}

  ngOnInit(): void {}

  createNewDrawing() {
    this.dialog.open(NewDrawingComponent, {});
  }
}
