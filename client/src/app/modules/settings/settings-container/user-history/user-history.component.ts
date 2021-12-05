import { Component, OnInit } from "@angular/core";
import { MatDialog, MatDialogRef } from "@angular/material/dialog";
import { Router } from "@angular/router";
import { JoinDrawingDialogComponent } from "src/app/modules/cards/drawing-card/join-drawing-dialog/join-drawing-dialog.component";
import { UsersService } from "src/app/modules/users/services/users.service";
import { Drawing } from "src/app/shared";

@Component({
  selector: "app-user-history",
  templateUrl: "./user-history.component.html",
  styleUrls: ["./user-history.component.scss"],
})
export class UserHistoryComponent implements OnInit {
  currentUser: any;
  userLoaded: Promise<boolean>;

  joinDrawingDialogRef: MatDialogRef<JoinDrawingDialogComponent>;

  constructor(
    private usersService: UsersService,
    private dialog: MatDialog,
    private router: Router
  ) {
    this.usersService
      .getUser(localStorage.getItem("userId")!)
      .subscribe((user) => {
        this.currentUser = user;
        this.userLoaded = Promise.resolve(true);
      });
  }

  ngOnInit(): void {}

  goToDrawing(drawing: Drawing) {
    if (drawing.privacyLevel == "protected") {
      this.joinDrawingDialogRef = this.dialog.open(JoinDrawingDialogComponent, {
        data: { drawing: drawing },
      });
    } else {
      this.router.navigate([`/drawings/${drawing._id}`]);
    }
  }
}
