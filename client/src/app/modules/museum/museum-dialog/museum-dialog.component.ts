import { DrawingService } from "src/app/modules/workspace";
import { Component, OnInit, Inject } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { PostService } from "../services/post.service";
import { DrawingHttpClientService } from "src/app/modules/backend-communication";

@Component({
  selector: "app-museum-dialog",
  templateUrl: "./museum-dialog.component.html",
  styleUrls: ["./museum-dialog.component.scss"],
})
export class MuseumDialog implements OnInit {
  constructor(
    public dialogRef: MatDialogRef<MuseumDialog>,
    @Inject(MAT_DIALOG_DATA) public drawingId: string,
    private postService: PostService,
    private drawingService: DrawingService,
    private drawingHttpClient: DrawingHttpClientService
  ) {}

  ngOnInit(): void {}

  async onPublishClick(): Promise<void> {
    await this.drawingService.saveDrawing();
    this.drawingHttpClient.getDrawing(this.drawingId).subscribe((drawing) => {
      this.postService
        .publishDrawing(this.drawingService.drawingId, drawing)
        .subscribe((response) => {
          this.dialogRef.close();
        });
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
