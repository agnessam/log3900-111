import { Component, Inject, OnInit } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { UsersService } from "src/app/modules/users/services/users.service";

@Component({
  selector: "app-edit-parameter-dialog",
  templateUrl: "./edit-parameter-dialog.component.html",
  styleUrls: ["./edit-parameter-dialog.component.scss"],
})
export class EditParameterDialogComponent implements OnInit {
  parameterForm: FormGroup;

  constructor(
    @Inject(MAT_DIALOG_DATA)
    public data: { username?: string; description?: string },
    private dialogRef: MatDialogRef<EditParameterDialogComponent>,
    private usersService: UsersService
  ) {}

  ngOnInit(): void {
    this.parameterForm = new FormGroup({
      parameter: new FormControl("", [
        Validators.required,
        Validators.minLength(3),
      ]),
    });
  }

  onAccept(): void {
    let newValues;
    if (this.data.description) {
      newValues = {
        description: this.parameterForm.value.parameter,
      };
    } else {
      newValues = {
        username: this.parameterForm.value.parameter,
      };
    }

    this.usersService
      .updateUser(localStorage.getItem("userId")!, newValues)
      .subscribe((updatedUser) => {
        this.dialogRef.close(updatedUser);
        this.parameterForm.reset();
      });
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
