import { Component, Inject, OnInit } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { Avatar } from "src/app/shared/models/avatar.model";

interface NgxAvatarInformation {
  sourceId: string;
  sourceType: string;
}

@Component({
  selector: "app-avatar-dialog",
  templateUrl: "./avatar-dialog.component.html",
  styleUrls: ["./avatar-dialog.component.scss"],
})
export class AvatarDialogComponent implements OnInit {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { avatars: Avatar[] },
    private dialogRef: MatDialogRef<AvatarDialogComponent>
  ) {}

  ngOnInit(): void {}

  onAvatarClicked(avatarInformation: NgxAvatarInformation) {
    // Event is the information about the avatar
    this.dialogRef.close(avatarInformation);
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
