import { Component, Inject, OnInit } from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { User } from "../../models/user";
import { UsersService } from "../../services/users.service";

@Component({
  selector: "app-confirm-unfollow-dialog",
  templateUrl: "./confirm-unfollow-dialog.component.html",
  styleUrls: ["./confirm-unfollow-dialog.component.scss"],
})
export class ConfirmUnfollowDialogComponent implements OnInit {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: User,
    private usersService: UsersService,
    private dialog: MatDialogRef<ConfirmUnfollowDialogComponent>
  ) {}

  ngOnInit(): void {}

  unfollowUser(): any {
    this.usersService.unfollowUser(this.data._id!).subscribe((user) => {
      this.dialog.close(user);
    });
  }

  onCancel(): void {
    this.dialog.close();
  }
}
