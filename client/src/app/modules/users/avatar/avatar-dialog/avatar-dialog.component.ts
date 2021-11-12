import { Component, Inject, OnInit } from "@angular/core";
import { MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Avatar } from "src/app/shared/models/avatar.model";

@Component({
  selector: "app-avatar-dialog",
  templateUrl: "./avatar-dialog.component.html",
  styleUrls: ["./avatar-dialog.component.scss"],
})
export class AvatarDialogComponent implements OnInit {
  constructor(@Inject(MAT_DIALOG_DATA) public data: { avatars: Avatar[] }) {}

  ngOnInit(): void {
    console.log(this.data);
  }
}
