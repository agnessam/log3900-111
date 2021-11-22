import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { User } from "../models/user";
import { UsersService } from "../services/users.service";

@Component({
  selector: "app-user-profile",
  templateUrl: "./user-profile.component.html",
  styleUrls: ["./user-profile.component.scss"],
})
export class UserProfileComponent implements OnInit {
  user: User;
  userId: string;

  constructor(
    private activatedRoute: ActivatedRoute,
    private usersService: UsersService
  ) {
    this.activatedRoute.params.subscribe((params) => {
      this.userId = params["id"];
      this.usersService.getUser(this.userId).subscribe((user) => {
        this.user = user;
      });
      this.usersService.getUserDrawings(this.userId).subscribe((drawings) => {
        this.user.drawings = drawings;
      });
    });
  }

  ngOnInit(): void {}
}
