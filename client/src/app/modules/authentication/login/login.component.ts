import { Component, OnInit, ViewEncapsulation } from "@angular/core";
import { FormGroup, FormControl, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthenticationService } from "../services/authentication/authentication.service";

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.scss"],
  encapsulation: ViewEncapsulation.None,
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService
  ) {}

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      email: new FormControl("", Validators.required),
      password: new FormControl("", Validators.required),
    });
  }

  onSubmit() {
    if (this.loginForm.invalid) {
      return;
    }

    this.authenticationService
      .login(this.loginForm.value.email, this.loginForm.value.password)
      .subscribe((response) => {
        if (response.error) {
          if (response.error == "Email was not found") {
            this.loginForm.controls["email"].setErrors({
              emailUnfindable: true,
            });
          } else if (response.error == "Wrong password") {
            this.loginForm.controls["password"].setErrors({
              wrongPassword: true,
            });
          }
        } else {
          this.router.navigate([""]);
        }
      });
  }
}
