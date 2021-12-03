import { Component, Inject, OnInit } from "@angular/core";
import { FormControl, FormGroup } from "@angular/forms";
import { MatDialogRef, MAT_DIALOG_DATA } from "@angular/material/dialog";
import { TeamClientService } from "src/app/modules/backend-communication/team-client/team-client.service";
import { Team } from "src/app/shared/models/team.model";
import { EditableTeamParameters } from "../../EditableTeam";

@Component({
  selector: "app-edit-team-parameters",
  templateUrl: "./edit-team-parameters.component.html",
  styleUrls: ["./edit-team-parameters.component.scss"],
})
export class EditTeamParametersComponent implements OnInit {
  teamForm: FormGroup;
  isPrivate: boolean;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: Team,
    private dialog: MatDialogRef<EditTeamParametersComponent>,
    private teamClient: TeamClientService
  ) {
    this.isPrivate = data.isPrivate;
  }

  ngOnInit(): void {
    this.teamForm = new FormGroup({
      description: new FormControl(this.data.description),
      isPrivate: new FormControl(this.data.isPrivate),
      password: new FormControl(this.data.password),
    });

    if (!this.isPrivate) {
      this.teamForm.get("password")?.disable();
    }
  }

  togglePrivacy(): void {
    this.isPrivate = !this.isPrivate;
    if (this.isPrivate) {
      this.teamForm.get("password")?.enable();
    } else {
      this.teamForm.get("password")?.disable();
    }
  }

  updateTeamParameters() {
    if (!this.isPrivate) {
      this.teamForm.value.password = "";
    }
    const teamParameters = new EditableTeamParameters(this.teamForm.value);
    this.teamClient
      .updateTeam(this.data._id, teamParameters)
      .subscribe((updatedTeam) => {
        this.dialog.close(updatedTeam);
        this.teamForm.reset();
      });
  }

  onCancel(): void {
    this.dialog.close();
  }
}
