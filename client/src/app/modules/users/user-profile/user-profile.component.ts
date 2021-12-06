import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { MatDialog, MatDialogRef } from "@angular/material/dialog";
import { ActivatedRoute, Router } from "@angular/router";
import { Drawing } from "src/app/shared";
import { User } from "../models/user";
import { UsersService } from "../services/users.service";
import { ConfirmUnfollowDialogComponent } from "./confirm-unfollow-dialog/confirm-unfollow-dialog.component";
import { PostInterface } from "../../museum/models/post.model";
import { PostDialogComponent } from "../../post-dialog/post-dialog.component";
import { PostService } from "../../museum/services/post.service";
import { Team } from "src/app/shared/models/team.model";

@Component({
  selector: "app-user-profile",
  templateUrl: "./user-profile.component.html",
  styleUrls: ["./user-profile.component.scss"],
})
export class UserProfileComponent implements OnInit {
  user: User;
  userId: string;
  filteredDrawings: Drawing[] = [];

  userLoaded: Promise<boolean>;

  confirmUnfollowDialog: MatDialogRef<ConfirmUnfollowDialogComponent>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private usersService: UsersService,
    private dialog: MatDialog,
    private changeDetector: ChangeDetectorRef,
    private postService: PostService
  ) {
    this.activatedRoute.params.subscribe((params) => {
      this.userId = localStorage.getItem("userId")!;
      const currentUserId = params["id"];
      this.usersService.getUser(currentUserId).subscribe((user) => {
        this.user = user;
        this.filterPrivateDrawings(this.user.drawings as Drawing[]);
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
    this.filteredDrawings.splice(
      this.filteredDrawings.findIndex((x) => x._id === deletedDrawing._id),
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

  openPostDialog(post: PostInterface): void {
    this.postService.getPostById(post._id).subscribe((postReceive) => {
      this.dialog.open(PostDialogComponent, {
        width: "80%",
        data: postReceive,
      });
    });
  }

  filterPrivateDrawings(drawings: Drawing[]) {
    console.log(drawings);
    for (let drawing of drawings) {
      if (this.hasPermission(drawing)) {
        this.filteredDrawings.push(drawing);
      }
    }
  }

  hasPermission(drawing: Drawing): boolean {
    if (drawing.privacyLevel != "private") return true;

    if (
      drawing.ownerModel == "User" &&
      (drawing.owner as User)._id == this.userId
    ) {
      return true;
    }

    if (drawing.ownerModel == "Team") {
      if (((drawing.owner as Team).members as string[]).includes(this.userId)) {
        return true;
      }
    }

    return false;
  }
}
