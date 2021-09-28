import { Component, OnInit } from "@angular/core";
import { AuthenticationService } from "src/app/services/authentication/authentication.service";

@Component({
  selector: "app-navbar",
  templateUrl: "./navbar.component.html",
  styleUrls: ["./navbar.component.scss"],
})
export class NavbarComponent implements OnInit {
  public username: string | null;
  constructor(private authenticationService: AuthenticationService) {
    this.authenticationService.currentUserObservable.subscribe(
      (username) => (this.username = username)
    );
  }

  ngOnInit(): void {}
}
