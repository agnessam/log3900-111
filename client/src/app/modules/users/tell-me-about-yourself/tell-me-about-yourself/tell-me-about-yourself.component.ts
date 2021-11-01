import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup } from "@angular/forms";
import { Router } from "@angular/router";
import { AuthenticationService } from "src/app/modules/authentication";
import { User } from "src/app/modules/authentication/models/user";
import { EditableUserParameters } from "../../models/editable-user-parameters";
import { UsersService } from "../../services/users.service";

@Component({
  selector: "app-tell-me-about-yourself",
  templateUrl: "./tell-me-about-yourself.component.html",
  styleUrls: ["./tell-me-about-yourself.component.scss"],
})
export class TellMeAboutYourselfComponent implements OnInit {
  currentUser: User | null;
  customizeProfileForm: FormGroup;

  constructor(
    private userService: UsersService,
    private authenticationService: AuthenticationService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.authenticationService.currentUserObservable.subscribe((user) => {
      this.currentUser = user;
    });
    this.customizeProfileForm = new FormGroup({
      description: new FormControl(""),
    });
  }

  onSubmit(): void {
    const updatedUserParameters = new EditableUserParameters(
      this.customizeProfileForm.value
    );
    this.userService
      .updateUser(this.currentUser!._id, updatedUserParameters)
      .subscribe((response) => {
        this.router.navigate([""]);
      });
  }
}
