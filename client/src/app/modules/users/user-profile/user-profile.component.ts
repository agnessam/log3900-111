import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { MatDialog, MatDialogRef } from "@angular/material/dialog";
import { ActivatedRoute, Router } from "@angular/router";
import { Drawing } from "src/app/shared";
import { User } from "../models/user";
import { UsersService } from "../services/users.service";
import { ConfirmUnfollowDialogComponent } from "./confirm-unfollow-dialog/confirm-unfollow-dialog.component";

@Component({
  selector: "app-user-profile",
  templateUrl: "./user-profile.component.html",
  styleUrls: ["./user-profile.component.scss"],
})
export class UserProfileComponent implements OnInit {
  user: User;
  userId: string;

  userLoaded: Promise<boolean>;

  confirmUnfollowDialog: MatDialogRef<ConfirmUnfollowDialogComponent>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private usersService: UsersService,
    private dialog: MatDialog,
    private changeDetector: ChangeDetectorRef
  ) {
    this.activatedRoute.params.subscribe((params) => {
      this.userId = localStorage.getItem("userId")!;
      const currentUserId = params["id"];
      this.usersService.getUser(currentUserId).subscribe((user) => {
        this.user = user;
        this.userLoaded = Promise.resolve(true);
      });
    });
  }

  ngOnInit(): void {}

  followUser(): any {
    this.usersService.followUser(this.user._id!).subscribe((user) => {
      this.user.followers = user.followers;
      this.changeDetector.detectChanges();
    });
  }

  unfollowUser(): any {
    this.usersService.unfollowUser(this.user._id!).subscribe((user) => {
      this.user.followers = user.followers;
      this.changeDetector.detectChanges();
    });
  }

  openConfirmUnfollowDialog() {
    this.confirmUnfollowDialog = this.dialog.open(
      ConfirmUnfollowDialogComponent,
      {
        data: this.user,
      }
    );

    this.confirmUnfollowDialog.afterClosed().subscribe((user: User) => {
      this.user.followers = user.followers;
      this.changeDetector.detectChanges();
    });
  }

  deleteDrawingFromView(deletedDrawing: Drawing) {
    this.user.drawings!.splice(
      (this.user.drawings! as Drawing[]).indexOf(deletedDrawing),
      1
    );
    this.changeDetector.detectChanges();
  }

  isMyProfile(): boolean {
    return this.user._id == localStorage.getItem("userId");
  }

  isFollowing(): boolean {
    return (this.user.followers as string[]).includes(this.userId);
  }

  navigateToSettingsPage() {
    this.router.navigate(["/settings/overview"]);
  }
}
