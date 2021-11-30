import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
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

  userLoaded: Promise<boolean>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private usersService: UsersService
  ) {
    this.activatedRoute.params.subscribe((params) => {
      this.userId = params["id"];
      this.usersService.getUser(this.userId).subscribe((user) => {
        this.user = user;
        this.userLoaded = Promise.resolve(true);
      });
    });
  }

  ngOnInit(): void {}

  isMyProfile(): boolean {
    return this.user._id == localStorage.getItem("userId");
  }

  navigateToSettingsPage() {
    this.router.navigate(["/settings/overview"]);
  }
}
