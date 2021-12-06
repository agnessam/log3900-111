import { Component, OnInit, ViewEncapsulation } from "@angular/core";
import {
  AbstractControl,
  FormControl,
  FormGroup,
  FormGroupDirective,
  NgForm,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from "@angular/forms";
import { ErrorStateMatcher } from "@angular/material/core";
import { Router } from "@angular/router";
import { AuthenticationService } from "../services/authentication/authentication.service";
import { trigger,state,style,transition,animate,keyframes } from    '@angular/animations';

export class RegisterErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(
    control: FormControl | null,
    form: FormGroupDirective | NgForm | null
  ): boolean {
    const invalidCtrl = !!(
      (control?.invalid && control?.touched) ||
      (control?.parent?.invalid && control?.touched)
    );
    return invalidCtrl;
  }
}

@Component({
  selector: "app-register",
  templateUrl: "./register.component.html",
  styleUrls: ["./register.component.scss"],
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
export class RegisterComponent implements OnInit {
  userRegistration: FormGroup;
  matcher = new RegisterErrorStateMatcher();

  isFirstNameValid :boolean = true;
  isLastNameValid :boolean = true;
  isUsernameValid :boolean = true;
  isEmailValid :boolean = true;
  isPasswordValid :boolean = true;
  isConfirmPasswordValid :boolean = true;

  usernameError:boolean=false;
  emailError:boolean= false;

  constructor(
    private authenticationService: AuthenticationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.userRegistration = new FormGroup(
      {
        username: new FormControl("", [
          Validators.required,
          Validators.minLength(4),
          Validators.maxLength(12),
        ]),
        password: new FormControl("", [
          Validators.required,
          Validators.minLength(8),
        ]),
        confirmPassword: new FormControl("", [Validators.required]),
        email: new FormControl("", [
          Validators.required,
          Validators.pattern("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$"),
        ]),
        firstName: new FormControl("", [Validators.required]),
        lastName: new FormControl("", [Validators.required]),
      },
      { validators: this.checkPasswords }
    );
  }

  onSubmit(): void {
    if (this.userRegistration.invalid) {
      return;
    }
    this.authenticationService
      .register(
        this.userRegistration.value.username,
        this.userRegistration.value.password,
        this.userRegistration.value.email,
        this.userRegistration.value.firstName,
        this.userRegistration.value.lastName
      )
      .subscribe((response) => {
        this.usernameError =false;
        this.emailError=false;

        if (response.error) {
          if ((response.error as string).includes("username")) {
            this.usernameError =true;
            this.isUsernameValid = !this.isUsernameValid;
            this.userRegistration.controls["username"].setErrors({
              duplicateUsername: true,
            });

          } else if ((response.error as string).includes("email")) {
            this.emailError=true;
            this.isEmailValid = !this.isEmailValid;
            this.userRegistration.controls["email"].setErrors({
              duplicateEmail: true,
            });

          }
        } else {
          this.router.navigate(["/users/customize"]);
        }
      });
  }

  checkPasswords: ValidatorFn = (
    group: AbstractControl
  ): ValidationErrors | null => {
    let newPassword = group.get("password")?.value;
    let confirmPassword = group.get("confirmPassword")?.value;
    return newPassword === confirmPassword ? null : { notSame: true };
  };

  validate(){
    if(this.userRegistration.hasError('required', 'firstName')){
      this.isFirstNameValid = !this.isFirstNameValid;
    }
    if(this.userRegistration.hasError('required', 'lastName')){
      this.isLastNameValid = !this.isLastNameValid;
    }
    if(this.userRegistration.hasError('required', 'username') ||
      this.userRegistration.hasError('minlength', 'username') ||
      this.userRegistration.hasError('maxlength', 'username') ||
      this.usernameError)
    {
      this.isUsernameValid = !this.isUsernameValid;
    }
    if(this.userRegistration.hasError('required', 'email') ||
      this.userRegistration.hasError('pattern', 'email') ||
      this.emailError)
    {
      this.isEmailValid = !this.isEmailValid;
    }
    if(this.userRegistration.hasError('required', 'password') ||
      this.userRegistration.hasError('minlength', 'password')){
      this.isPasswordValid = !this.isPasswordValid;
    }
    if(this.userRegistration.hasError('notSame')){
      this.isConfirmPasswordValid = !this.isConfirmPasswordValid;
    }

  }
}
