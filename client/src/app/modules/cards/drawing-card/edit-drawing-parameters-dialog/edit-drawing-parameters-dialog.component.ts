import { Component, Inject, OnInit } from "@angular/core";
import {
  AbstractControl,
  FormControl,
  FormGroup,
  ValidationErrors,
  ValidatorFn,
  Validators,
} from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { DrawingHttpClientService } from "src/app/modules/backend-communication";
import { OptionalDrawingParameters } from "src/app/modules/new-drawing/optional-drawing-parameters";
import { FormErrorStateMatcher } from "src/app/modules/settings/settings-container/user-overview/error-state-matcher/ErrorStateMatcher";
import { Drawing } from "src/app/shared";

@Component({
  selector: "app-edit-drawing-parameters-dialog",
  templateUrl: "./edit-drawing-parameters-dialog.component.html",
  styleUrls: ["./edit-drawing-parameters-dialog.component.scss"],
})
export class EditDrawingParametersDialogComponent implements OnInit {
  privacyLevel: string;
  drawingForm: FormGroup;

  matcher = new FormErrorStateMatcher();

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { drawing: Drawing },
    private dialogRef: MatDialogRef<EditDrawingParametersDialogComponent>,
    private drawingClient: DrawingHttpClientService
  ) {
    this.privacyLevel = data.drawing.privacyLevel;
  }

  ngOnInit(): void {
    this.drawingForm = new FormGroup(
      {
        name: new FormControl(this.data.drawing.name, [
          Validators.required,
          Validators.min(4),
          Validators.maxLength(12),
        ]),
        privacyLevel: new FormControl(this.data.drawing.privacyLevel),
        password: new FormControl(this.data.drawing.password),
      },
      { validators: this.NonNullPasswordIfProtected }
    );

    if (this.drawingForm.get("privacyLevel")?.value != "protected") {
      this.drawingForm.get("password")?.disable();
    }
  }

  changePrivacyLevel() {
    if (this.privacyLevel == "protected") {
      this.drawingForm.get("password")?.enable();
    } else {
      this.drawingForm.get("password")?.disable();
    }
  }

  updateDrawingParameters() {
    const drawingParameters = new OptionalDrawingParameters(
      this.drawingForm.value
    );
    this.drawingClient
      .updateDrawingParameters(this.data.drawing._id, drawingParameters)
      .subscribe((drawing) => {
        this.dialogRef.close(drawing);
        this.drawingForm.reset();
      });
  }

  onCancel(): void {
    this.dialogRef.close();
    this.drawingForm.reset();
  }

  NonNullPasswordIfProtected: ValidatorFn | null = (
    group: AbstractControl
  ): ValidationErrors | null => {
    let privacy = group.get("privacyLevel")?.value;
    if (privacy == "protected") {
      if (group.get("password")?.value == "") {
        return { nullPassword: true };
      }
    }
    return null;
  };
}
