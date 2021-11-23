import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { PostInterface } from '../models/post.model';
import { CommentInterface } from '../models/comment.model';


@Injectable({
  providedIn: "root",
})

export class PostService{

  private endpointUrl: string = environment.serverURL + "/posts";
  private httpHeaders: HttpHeaders = new HttpHeaders().set(
    "ContentType",
    "application/x-www-form-urlencoded",
  );

  constructor(private httpClient: HttpClient) {}

  postCanvas(name:string):void {
    console.log("drawing name " + name + " has been post");
  }

  getPosts(): Observable<PostInterface[]> {
    return this.httpClient
      .get<PostInterface[]>(this.endpointUrl)
      .pipe((response) => {
        return response;
      });
  }

  addComment(userId: string, postId: string, comment: CommentInterface): Observable<PostInterface> {
    return this.httpClient
      .post<PostInterface>(`${this.endpointUrl}/${postId}/comments`, {
        userId: userId,
        postId: postId,
        comment: comment
      }, {
        headers: this.httpHeaders,
      })
      .pipe((response) => {
        return response;
      });
  }

  addLike(userId: string, postId: string, ):Observable<PostInterface>{
    return this.httpClient
      .post<PostInterface>(`${this.endpointUrl}/${postId}/likes`, {
        userId: userId,
        postId: postId
      }, {
        headers: this.httpHeaders,
      })
      .pipe((response) => {
        return response;
      });
  }

  removeLike(userId: string, postId: string, ):Observable<PostInterface>{
    return this.httpClient
      .post<PostInterface>(`${this.endpointUrl}/${postId}/likes`, {
        userId: userId,
        postId: postId
      }, {
        headers: this.httpHeaders,
      })
      .pipe((response) => {
        return response;
      });
  }

}
