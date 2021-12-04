import { PostService } from "./../museum/services/post.service";
import {
  Component,
  OnInit,
  Inject,
  ViewChild,
  ElementRef,
} from "@angular/core";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { PostInterface } from "../museum/models/post.model";
import { DomSanitizer } from "@angular/platform-browser";
import { AuthenticationService } from "../authentication";
import { User } from "../authentication/models/user";
import { MatSnackBar } from "@angular/material/snack-bar";
import { CommentInterface } from "../museum/models/comment.model";
import { Team } from "src/app/shared/models/team.model";

@Component({
  selector: "app-post-dialog",
  templateUrl: "./post-dialog.component.html",
  styleUrls: ["./post-dialog.component.scss"],
})
export class PostDialogComponent implements OnInit {
  @ViewChild("commentInput", { static: false })
  private commentInput: ElementRef<HTMLInputElement>;
  @ViewChild("audioOption") audioPlayerRef: ElementRef;

  user: User | null;

  constructor(
    public dialogRef: MatDialogRef<PostDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public post: PostInterface,
    private sanitizer: DomSanitizer,
    private postService: PostService,
    private authenticationService: AuthenticationService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.dialogRef.backdropClick().subscribe((_) => {
      this.dialogRef.close(this.post);
    });
    this.authenticationService.currentUserObservable.subscribe(
      (user) => (this.user = user)
    );
  }

  transformUri() {
    let safeDataUri = this.sanitizer.bypassSecurityTrustUrl(this.post.dataUri);
    return safeDataUri;
  }

  toggleLike() {
    const userid = (this.user as User)._id;

    if (this.post.likes.includes(userid)) {
      this.postService.removeLike(userid, this.post._id).subscribe((like) => {
        const index = this.post.likes.findIndex(
          (like) => like === userid
        ) as number;
        this.post.likes.splice(index, 1);
      });
    } else {
      this.onAudioPlay();
      this.postService.addLike(userid, this.post._id).subscribe((like) => {
        this.post.likes.push(userid);
      });
    }
  }

  hasBeenLike(likes: string[]): boolean {
    if (this.user?._id == null) return false;
    return likes.includes(this.user?._id);
  }

  addComment() {
    if (this.user?._id == null) return;
    let input = this.commentInput.nativeElement.value;
    this.commentInput.nativeElement.value = "";

    const isWhitespace = (input || "").trim().length === 0;
    if (isWhitespace) {
      this.snackBar.open("The comment can not be empty", "Close", {
        duration: 3000,
      });
      return;
    }
    const comment = {
      content: input,
      author: this.user,
      postId: this.post._id,
    } as CommentInterface;
    this.postService
      .addComment(this.post._id, comment)
      .subscribe((commentReceive) => {
        this.post.comments.push(comment);
      });
  }

  getAuthorUsername(comment: CommentInterface) {
    let author = comment.author as User;
    return author.username;
  }

  getUsername(post: PostInterface): string {
    if (post.ownerModel === "Team") {
      let owner = post.owner as Team;
      return owner.name;
    } else {
      let owner = post.owner as User;
      return owner.username;
    }
  }

  getAvatar(post: PostInterface): string {
    if (post.ownerModel === "User") {
      let owner = post.owner as User;
      return owner.avatar.imageUrl;
    } else if (post.ownerModel === "Team") {
      return (post.owner as Team).name;
    }
    return "";
  }

  getTime(createdAt: string): string {
    return createdAt.split("T")[0];
  }

  onAudioPlay() {
    this.audioPlayerRef.nativeElement.play();
  }
}
