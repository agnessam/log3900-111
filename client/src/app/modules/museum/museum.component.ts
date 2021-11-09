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
    image:"../../../../assets/img/test.jpg",
    timestamp:"19:15",
    author:"patate",
    like:45,
    comments:[{message: "woow", author:"joee"}, {message:"amazing", author:"lynnn"}]
  }
  const post2: MuseumPost = {
    image:"../../../../assets/img/test2.png",
    timestamp:"20:27",
    author:"harry",
    like:17,
    comments:[{message: "nice", author:"joee"}, {message:"cool", author:"nada"}]
  }
    this.posts = [];
    this.posts.push(post);
    this.posts.push(post2);
  }

  ngOnInit(): void {
    this.authenticationService.currentUserObservable.subscribe(
      (user) => (this.user = user),
    );
  }

}
