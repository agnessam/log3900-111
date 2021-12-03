import { Component, OnInit } from "@angular/core";
import { FormGroup, FormControl, Validators } from "@angular/forms";
import { MatDialogRef } from "@angular/material/dialog";
import { TextChannel } from "../../models/text-channel.model";
import { TextChannelService } from "../../services/text-channel.service";

@Component({
  selector: "app-new-channel",
  templateUrl: "./new-channel.component.html",
  styleUrls: ["./new-channel.component.scss"],
})
export class NewChannelComponent implements OnInit {
  newDrawingForm: FormGroup;
  existingChannels: TextChannel[];

  constructor(
    private textChannelService: TextChannelService,
    private dialogRef: MatDialogRef<NewChannelComponent>
  ) {}

  ngOnInit(): void {
    this.newDrawingForm = new FormGroup({
      name: new FormControl("", [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(12),
      ]),
    });
    this.textChannelService
      .getChannels()
      .subscribe((channels) => (this.existingChannels = channels));
  }

  onAccept(): void {
    if (
      this.existingChannels.find(
        (channel) => channel.name === this.newDrawingForm.value.name
      )
    ) {
      this.newDrawingForm.controls["name"].setErrors({
        duplicateChannelName: true,
      });
      return;
    }

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
}
