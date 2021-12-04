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
import { FormErrorStateMatcher } from "src/app/modules/settings/settings-container/user-overview/error-state-matcher/ErrorStateMatcher";
import { TextChannel } from "../../models/text-channel.model";
import { TextChannelService } from "../../services/text-channel.service";

@Component({
  selector: "app-new-channel",
  templateUrl: "./new-channel.component.html",
  styleUrls: ["./new-channel.component.scss"],
})
export class NewChannelComponent implements OnInit {
  newDrawingForm: FormGroup;

  matcher = new FormErrorStateMatcher();

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: TextChannel[],
    private textChannelService: TextChannelService,
    private dialogRef: MatDialogRef<NewChannelComponent>
  ) {}

  ngOnInit(): void {
    this.newDrawingForm = new FormGroup(
      {
        name: new FormControl("", [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(12),
        ]),
      },
      { validators: this.NoDuplicateName }
    );
  }

  onAccept(): void {
    if (this.newDrawingForm.invalid) {
      return;
    }

    this.textChannelService
      .createChannel(
        this.newDrawingForm.value.name,
        localStorage.getItem("userId")!
      )
      .subscribe((response) => {
        this.dialogRef.close(response);
        this.newDrawingForm.reset();
      });
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  NoDuplicateName: ValidatorFn = (
    group: AbstractControl
  ): ValidationErrors | null => {
    const channelName = group.get("name")?.value as string;

    for (let i = 0; i < this.data.length; ++i) {
      if (this.data[i].name.toLowerCase() == channelName.toLowerCase()) {
        return { duplicateNameError: true };
      }
    }

    return null;
  };
}
