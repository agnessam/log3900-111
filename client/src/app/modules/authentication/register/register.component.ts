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
  encapsulation: ViewEncapsulation.None,
})
export class RegisterComponent implements OnInit {
  userRegistration: FormGroup;
  matcher = new RegisterErrorStateMatcher();

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
        if (response.error) {
          if ((response.error as string).includes("username")) {
            this.userRegistration.controls["username"].setErrors({
              duplicateUsername: true,
            });
          } else if ((response.error as string).includes("email")) {
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
}
