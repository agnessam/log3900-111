import { MatDialog } from "@angular/material/dialog";
import { NewDrawingComponent } from "../../new-drawing";
import { Component, AfterViewInit } from "@angular/core";
import { DrawingHttpClientService } from "../../backend-communication";
import { Drawing } from "src/app/shared";
import { DomSanitizer, SafeUrl } from "@angular/platform-browser";

@Component({
  selector: "app-gallery",
  templateUrl: "./gallery.component.html",
  styleUrls: ["./gallery.component.scss"],
})
export class GalleryComponent implements AfterViewInit {
  drawings: Array<Drawing> = [];
  constructor(
    private dialog: MatDialog,
    private drawingHttpClient: DrawingHttpClientService,
    private sanitizer: DomSanitizer
  ) {}

  ngAfterViewInit(): void {
    this.drawingHttpClient.getDrawings().subscribe((response) => {
      for (let drawing of response) {
        this.drawings.push(drawing);
      }
    });
  }

  getSanitizedUrl(dataUri: string): SafeUrl {
    return this.sanitizer.bypassSecurityTrustUrl(dataUri);
  }

  createNewDrawing() {
    this.dialog.open(NewDrawingComponent, {});
  }
}
