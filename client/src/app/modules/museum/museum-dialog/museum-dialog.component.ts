import { Component, Inject, OnInit } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { DrawingHttpClientService } from "src/app/modules/backend-communication";
import { DrawingService } from "src/app/modules/workspace";
import { Drawing } from "src/app/shared";
import { PostService } from "../services/post.service";

@Component({
  selector: "app-museum-dialog",
  templateUrl: "./museum-dialog.component.html",
  styleUrls: ["./museum-dialog.component.scss"],
})
export class MuseumDialog implements OnInit {
  postForm: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<MuseumDialog>,
    @Inject(MAT_DIALOG_DATA) public data: Drawing,
    private postService: PostService,
    private drawingService: DrawingService,
    private drawingHttpClient: DrawingHttpClientService
  ) {}

  ngOnInit(): void {
    console.log(this.postService);
    console.log(this.drawingService);
    console.log(this.drawingHttpClient);
    this.postForm = new FormGroup({
      name: new FormControl("", [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(12),
      ]),
    });
  }

  onPublishClick(): void {
    if (this.postForm.invalid) return;
    this.data.name = this.postForm.value.name;
    this.postService
      .publishDrawing(this.data._id, this.data)
      .subscribe((post) => {
        this.dialogRef.close(post);
        this.postForm.reset();
      });
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
