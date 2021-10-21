import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { User } from "../../authentication/models/user";

@Component({
  selector: "app-user-profile",
  templateUrl: "./user-profile.component.html",
  styleUrls: ["./user-profile.component.scss"],
})
export class UserProfileComponent implements OnInit {
  user: User;
  userId: string;

  constructor(private activatedRoute: ActivatedRoute) {
    activatedRoute.params.subscribe((params) => {
      this.userId = params["id"];
    });
  }

  ngOnInit(): void {
    console.log(this.activatedRoute);
    console.log(this.userId);
  }
}
