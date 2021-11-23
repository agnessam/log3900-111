import { PostService } from './services/post.service';
import { Component, OnInit } from '@angular/core';
import { PostInterface } from './models/post.model';
import { AuthenticationService } from '../authentication';
import { User } from '../authentication/models/user';
import { DomSanitizer } from '@angular/platform-browser';
import { CommentInterface } from './models/comment.model';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-museum',
  templateUrl: './museum.component.html',
  styleUrls: ['./museum.component.scss']
})
export class MuseumComponent implements OnInit {
  user: User | null;
  posts: PostInterface[];

  constructor(private authenticationService: AuthenticationService,
    private postService: PostService,
    private sanitizer: DomSanitizer,
    private snackBar: MatSnackBar,
    )
    {

    }

  ngOnInit(): void {
    this.authenticationService.currentUserObservable.subscribe(
      (user) => (this.user = user),
    );

    this.postService.getPosts().subscribe((posts) => {
      this.posts = posts;
    });
  }

  transformUri(dataUri: string){
    let safeDataUri = this.sanitizer.bypassSecurityTrustUrl(dataUri);
    return safeDataUri;
  }

  addComment(postId:string){
    if(this.user?._id == null) return;

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

    const comment = { content: content, authorId: this.user, postId: postId} as CommentInterface;
    this.postService.addComment(this.user?._id, "", comment).subscribe((comment) => {

    });
  }

  toggleLike(post: PostInterface){
    if(this.user?._id == null) return;

    if (post.likes.includes(this.user?._id)){
      console.log("unlike");
      this.postService.removeLike(this.user._id, post._id).subscribe((like) => {});
    }
    else{
      console.log("like");
      this.postService.addLike(this.user._id, post._id).subscribe((like) => {});
    }
  }

  hasBeenLike(likes: string[]): boolean{
    if(this.user?._id == null) return false;

    return likes.includes(this.user?._id);
  }
}
