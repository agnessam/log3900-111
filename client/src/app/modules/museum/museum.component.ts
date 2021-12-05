import { PostService } from "./services/post.service";
import { Component, OnInit, ViewChild, ElementRef } from "@angular/core";
import { PostInterface } from "./models/post.model";
import { AuthenticationService } from "../authentication";
import { User } from "../authentication/models/user";
import { DomSanitizer } from "@angular/platform-browser";
import { CommentInterface } from "./models/comment.model";
import { MatSnackBar } from "@angular/material/snack-bar";
import { MatDialog } from "@angular/material/dialog";
import { PostDialogComponent } from "../post-dialog/post-dialog.component";
import { Router } from "@angular/router";

@Component({
  selector: "app-museum",
  templateUrl: "./museum.component.html",
  styleUrls: ["./museum.component.scss"],
})
export class MuseumComponent implements OnInit {
  @ViewChild("audioOption") audioPlayerRef: ElementRef;

  user: User | null;
  posts: PostInterface[];

  constructor(
    private authenticationService: AuthenticationService,
    private postService: PostService,
    private sanitizer: DomSanitizer,
    private snackBar: MatSnackBar,
    public dialog: MatDialog,
    private router: Router
  ) {
    this.posts = new Array();
  }

  ngOnInit(): void {
    this.authenticationService.currentUserObservable.subscribe(
      (user) => (this.user = user)
    );

    this.postService.getPosts().subscribe((posts) => {
      this.posts = posts;
    });
  }

  transformUri(dataUri: string) {
    let safeDataUri = this.sanitizer.bypassSecurityTrustUrl(dataUri);
    return safeDataUri;
  }

  addComment(postId: string) {
    if (this.user?._id == null) return;

    let input = document.getElementById(postId) as HTMLInputElement;
    let content = input.value;
    input.value = "";

    const isWhitespace = (content || "").trim().length === 0;
    if (isWhitespace) {
      this.snackBar.open("The comment can not be empty", "Close", {
        duration: 3000,
      });
      return;
    }

    const comment = {
      content: content,
      author: this.user,
      postId: postId,
    } as CommentInterface;
    this.postService.addComment(postId, comment).subscribe((commentReceive) => {
      comment.createdAt = "";
      this.posts.find((post) => post._id === postId)?.comments.push(comment);
    });
  }

  toggleLike(post: PostInterface) {
    const userid = (this.user as User)._id;

    if (post.likes.includes(userid)) {
      this.postService.removeLike(userid, post._id).subscribe((like) => {
        const index = this.posts
          .find((postItem) => postItem._id === post._id)
          ?.likes.findIndex((like) => like === userid) as number;
        this.posts
          .find((postItem) => postItem._id === post._id)
          ?.likes.splice(index, 1);
      });
    } else {
      this.effect(post._id);
      this.postService.addLike(userid, post._id).subscribe((like) => {
        this.posts
          .find((postItem) => postItem._id === post._id)
          ?.likes.push(userid);
      });
    }
  }

  hasBeenLike(likes: string[]): boolean {
    if (this.user?._id == null) return false;
    return likes.includes(this.user?._id);
  }

  openPostDialog(post: PostInterface): void {
    this.dialog.open(PostDialogComponent, {
      width: "80%",
      data: post,
    });
  }

  effect(postId: string) {
    this.audioPlayerRef.nativeElement.play();
    let postLiked = document.getElementById(
      postId + ":like"
    ) as HTMLInputElement;
    postLiked.style.display = "block";
    setTimeout(() => {
      postLiked.style.display = "none";
    }, 1400);
  }

  goToUserProfile(post: PostInterface) {
    this.router.navigate([`/users/${post.owner._id}`]);
  }

  goToTeamProfile(post: PostInterface) {
    this.router.navigate([`/teams/${post.owner._id}`]);
  }

  goToUserProfileViaComment(comment: CommentInterface) {
    this.router.navigate([`/users/${comment.author._id}`]);
  }
}
