import { Component, OnInit, ViewEncapsulation } from "@angular/core";
import { FormControl, FormGroup } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthenticationService } from "../services/authentication/authentication.service";

@Component({
  selector: "app-register",
  templateUrl: "./register.component.html",
  styleUrls: ["./register.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService
  ) {}

  ngOnInit(): void {
    this.registerForm = new FormGroup({
      username: new FormControl(""),
      password: new FormControl(""),
      confirmPassword: new FormControl(""),
      email: new FormControl(""),
      firstName: new FormControl(""),
      lastName: new FormControl(""),
    });
  }

  onSubmit(): void {
    this.authenticationService.isAuthenticated();
    this.router.navigate(["/chat"]);
  }
}
