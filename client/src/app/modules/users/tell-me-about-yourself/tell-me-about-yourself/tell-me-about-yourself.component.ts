import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup } from "@angular/forms";
import { EditableUserParameters } from "../../models/user";
import { UsersService } from "../../services/users.service";

@Component({
  selector: "app-tell-me-about-yourself",
  templateUrl: "./tell-me-about-yourself.component.html",
  styleUrls: ["./tell-me-about-yourself.component.scss"],
})
export class TellMeAboutYourselfComponent implements OnInit {
  customizeProfileForm: FormGroup;

  constructor(private userService: UsersService) {}

  ngOnInit(): void {
    this.customizeProfileForm = new FormGroup({
      description: new FormControl(""),
    });
  }

  onSubmit(): void {
    const updatedUserParameters = new EditableUserParameters(
      this.customizeProfileForm.value
    );
    console.log(updatedUserParameters);
    this.userService.updateUser(updatedUserParameters);
  }
}
