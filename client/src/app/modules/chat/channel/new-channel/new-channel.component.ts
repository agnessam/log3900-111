import { Component, HostListener, OnInit } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { DrawingHttpClientService } from 'src/app/modules/backend-communication';
import { ColorPickerService } from 'src/app/modules/color-picker';
import { NewDrawingComponent, NewDrawingService } from 'src/app/modules/new-drawing';
import { UsersService } from 'src/app/modules/users/services/users.service';
import { DrawingService } from 'src/app/modules/workspace';
import { DEFAULT_RGB_COLOR, DEFAULT_ALPHA } from 'src/app/shared';
import { Team } from 'src/app/shared/models/team.model';
import { TextChannelService } from '../../services/text-channel.service';

@Component({
  selector: 'app-new-channel',
  templateUrl: './new-channel.component.html',
  styleUrls: ['./new-channel.component.scss']
})
export class NewChannelComponent implements OnInit {
  newDrawingForm: FormGroup;

  constructor(
    private textChannelService: TextChannelService,
    private dialogRef: MatDialogRef<NewChannelComponent>
  ) {}

  ngOnInit(): void {
    this.dialogRef.updateSize("50%", "30%");
    this.newDrawingForm = new FormGroup({
      name: new FormControl(""),
    });
  }

  onAccept(): void {
    this.textChannelService
      .createChannel(this.newDrawingForm.value.name, localStorage.getItem("userId")!)
      .subscribe((response) => {
        this.dialogRef.close(response);
        this.newDrawingForm.reset();
      });
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
