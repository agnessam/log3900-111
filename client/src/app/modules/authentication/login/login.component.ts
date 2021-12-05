import { Component, OnInit, ViewEncapsulation, ViewChild, ElementRef } from "@angular/core";
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
  @ViewChild("email") email: ElementRef;

  loginForm: FormGroup;
  emailFormField: HTMLInputElement;
  passwordFormField: HTMLInputElement;

  constructor(
    private router: Router,
    private authenticationService: AuthenticationService
  ) {
  }

  ngOnInit(): void {
    this.loginForm = new FormGroup({
      email: new FormControl("", Validators.required),
      password: new FormControl("", Validators.required),
    });

    this.emailFormField = document.getElementById("emailFormField") as HTMLInputElement;
    this.passwordFormField = document.getElementById("passwordFormField") as HTMLInputElement;
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
            this.shake(this.emailFormField);
            this.loginForm.controls["email"].setErrors({
              emailUnfindable: true,
            });
          } else if (response.error == "Wrong password") {
            this.shake(this.passwordFormField);
            this.loginForm.controls["password"].setErrors({
              wrongPassword: true,
            });
          }
        } else {
          this.router.navigate([""]);
        }
      });
  }

  shake(formField: HTMLInputElement){
    formField.classList.add('shake');
    setTimeout(() => {
      formField.classList.remove('shake');
    }, 1000);
  }
}
