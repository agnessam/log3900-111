import { Component, OnInit } from '@angular/core';
import { MuseumPost } from './models/museumPost.model';
import { AuthenticationService } from '../authentication';
import { User } from '../authentication/models/user';

@Component({
  selector: 'app-museum',
  templateUrl: './museum.component.html',
  styleUrls: ['./museum.component.scss']
})
export class MuseumComponent implements OnInit {
  user: User | null;
  posts: MuseumPost[];

  constructor(private authenticationService: AuthenticationService,) {
    const post: MuseumPost = {
    image:"image",
    timestamp:"19:15",
    author:"patate",
    like:45,
    comments:[{message: "woow", author:"joee"}, {message:"amazing", author:"lynnn"}]
  }
    this.posts = [];
    this.posts.push(post);
    this.posts.push(post);
  }

  ngOnInit(): void {
    this.authenticationService.currentUserObservable.subscribe(
      (user) => (this.user = user),
    );
  }

}
