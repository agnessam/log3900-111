import { Component, Inject, OnInit } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { DrawingHttpClientService } from "src/app/modules/backend-communication";
import { Drawing } from "src/app/shared";

@Component({
  selector: "app-confirm-delete-drawing-dialog",
  templateUrl: "./confirm-delete-drawing-dialog.component.html",
  styleUrls: ["./confirm-delete-drawing-dialog.component.scss"],
})
export class ConfirmDeleteDrawingDialogComponent implements OnInit {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: Drawing,
    private dialog: MatDialogRef<ConfirmDeleteDrawingDialogComponent>,
    private drawingService: DrawingHttpClientService
  ) {}

  ngOnInit(): void {}

  deleteDrawing() {
    this.drawingService
      .deleteDrawing(this.data._id)
      .subscribe((deletedDrawing) => {
        if (!deletedDrawing) return;
        this.dialog.close(deletedDrawing);
      });
  }

  onCancel() {
    this.dialog.close();
  }
}
