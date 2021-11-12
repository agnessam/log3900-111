import { Component, OnInit } from "@angular/core";
import { MatDialog, MatDialogRef } from "@angular/material/dialog";
import { AvatarClientService } from "../../backend-communication/avatar-client/avatar-client.service";
import { AvatarDialogComponent } from "./avatar-dialog/avatar-dialog.component";

@Component({
  selector: "app-avatar",
  templateUrl: "./avatar.component.html",
  styleUrls: ["./avatar.component.scss"],
})
export class AvatarComponent implements OnInit {
  chooseAvatarDialogRef: MatDialogRef<AvatarDialogComponent>;
  constructor(
    private avatarClient: AvatarClientService,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {}

  openChooseAvatarDialog() {
    this.avatarClient.getDefaultAvatars().subscribe((response) => {
      this.chooseAvatarDialogRef = this.dialog.open(AvatarDialogComponent, {
        width: "500px",
        height: "700px",
        data: {
          avatars: response,
        },
      });
    });
  }
}
