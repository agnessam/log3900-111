import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup } from "@angular/forms";
import { MatDialogRef } from "@angular/material/dialog";
import { MatSnackBar } from "@angular/material/snack-bar";
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
    private dialogRef: MatDialogRef<NewTeamComponent>,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.newTeamForm = new FormGroup({
      teamName: new FormControl(""),
      description: new FormControl(""),
    });
  }

  onAccept(): void {
    this.teamClient
      .createTeam(
        this.newTeamForm.value.teamName,
        this.newTeamForm.value.description
      )
      .subscribe(
        (response) => {
          this.dialogRef.close(response);
          this.newTeamForm.reset();
        },
        (error) => {
          this.snackBar.open("Team name is already in use", "Close", {
            duration: 3000,
          });
        }
      );
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
