import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { AuthenticationService } from "src/app/modules/authentication";
@Component({
  selector: "app-navbar",
  templateUrl: "./navbar.component.html",
  styleUrls: ["./navbar.component.scss"],
})
export class NavbarComponent implements OnInit {
  public username: string | null;
  constructor(
    private router: Router,
    private authenticationService: AuthenticationService
  ) {
    this.authenticationService.currentUserObservable.subscribe(
      (username) => (this.username = username)
    );
  }

  ngOnInit(): void {}

  logout(): void {
    this.authenticationService.logout(this.username).subscribe((response) => {
      console.log(response);
    });
    this.router.navigate(["/users/login"]);
    console.log("LOGOUT");
  }
}
