import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup } from "@angular/forms";
import { MatDialogRef } from "@angular/material/dialog";
import { TeamClientService } from "../../backend-communication/team-client/team-client.service";

@Component({
  selector: "app-new-team",
  templateUrl: "./new-team.component.html",
  styleUrls: ["./new-team.component.scss"],
})
export class NewTeamComponent implements OnInit {
  newTeamForm: FormGroup;

  constructor(
    private teamClient: TeamClientService,
    private dialogRef: MatDialogRef<NewTeamComponent>
  ) {}

  ngOnInit(): void {
    this.dialogRef.updateSize("15%", "30%");
    this.newTeamForm = new FormGroup({
      teamName: new FormControl(""),
      description: new FormControl(""),
    });
  }

  onAccept(): void {
    console.log(this.teamClient);

    console.log(this.newTeamForm.value);
    console.log("Accepted the form");
    this.teamClient
      .createTeam(
        this.newTeamForm.value.teamName,
        this.newTeamForm.value.description
      )
      .subscribe((response) => {
        this.dialogRef.close(response);
        this.newTeamForm.reset();
      });
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
