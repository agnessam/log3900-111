import { Component, OnInit, ViewEncapsulation, ViewChild, ElementRef } from "@angular/core";
import { FormGroup, FormControl, Validators } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthenticationService } from "../services/authentication/authentication.service";
import { trigger,state,style,transition,animate,keyframes } from    '@angular/animations';

@Component({
  selector: "app-login",
  templateUrl: "./login.component.html",
  styleUrls: ["./login.component.scss"],
  animations: [
    trigger('shakeit', [
        state('shake1', style({
            transform: 'scale(1)',
        })),
        state('shake2', style({
            transform: 'scale(1)',
        })),
        transition('shake1 => shake2', animate('1000ms ease-in',     keyframes([
          style({transform: 'translate3d(-1px, 0, 0)', offset: 0.1}),
          style({transform: 'translate3d(2px, 0, 0)', offset: 0.2}),
          style({transform: 'translate3d(-4px, 0, 0)', offset: 0.3}),
          style({transform: 'translate3d(4px, 0, 0)', offset: 0.4}),
          style({transform: 'translate3d(-4px, 0, 0)', offset: 0.5}),
          style({transform: 'translate3d(4px, 0, 0)', offset: 0.6}),
          style({transform: 'translate3d(-4px, 0, 0)', offset: 0.7}),
          style({transform: 'translate3d(2px, 0, 0)', offset: 0.8}),
          style({transform: 'translate3d(-1px, 0, 0)', offset: 0.9}),
        ]))),
        transition('shake2 => shake1', animate('1000ms ease-in',     keyframes([
          style({transform: 'translate3d(-1px, 0, 0)', offset: 0.1}),
          style({transform: 'translate3d(2px, 0, 0)', offset: 0.2}),
          style({transform: 'translate3d(-4px, 0, 0)', offset: 0.3}),
          style({transform: 'translate3d(4px, 0, 0)', offset: 0.4}),
          style({transform: 'translate3d(-4px, 0, 0)', offset: 0.5}),
          style({transform: 'translate3d(4px, 0, 0)', offset: 0.6}),
          style({transform: 'translate3d(-4px, 0, 0)', offset: 0.7}),
          style({transform: 'translate3d(2px, 0, 0)', offset: 0.8}),
          style({transform: 'translate3d(-1px, 0, 0)', offset: 0.9}),
        ]))),
  ])],
  encapsulation: ViewEncapsulation.None,
})

export class LoginComponent implements OnInit {
  @ViewChild("email") email: ElementRef;

  loginForm: FormGroup;

  isEmailValid: boolean = true;
  isPasswordValid: boolean = true;

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
            this.isEmailValid = !this.isEmailValid;

            this.loginForm.controls["email"].setErrors({
              emailUnfindable: true,
            });
          } else if (response.error == "Wrong password") {
            this.isPasswordValid = !this.isPasswordValid;
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
