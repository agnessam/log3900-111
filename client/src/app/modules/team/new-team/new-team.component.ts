import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import { MatDialogRef } from "@angular/material/dialog";
import { MatSnackBar } from "@angular/material/snack-bar";
import { TeamClientService } from "../../backend-communication/team-client/team-client.service";
import { EditableTeamParameters } from "../EditableTeam";

@Component({
  selector: "app-new-team",
  templateUrl: "./new-team.component.html",
  styleUrls: ["./new-team.component.scss"],
})
export class NewTeamComponent implements OnInit {
  newTeamForm: FormGroup;
  isMemberLimit: boolean = false;

  constructor(
    private teamClient: TeamClientService,
    private dialogRef: MatDialogRef<NewTeamComponent>,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit(): void {
    this.newTeamForm = new FormGroup({
      name: new FormControl("", [Validators.required]),
      description: new FormControl(""),
      memberLimit: new FormControl({ value: 1, disabled: true }, [
        Validators.min(1),
      ]),
    });
  }

  toggleMemberLimit(): void {
    this.isMemberLimit = !this.isMemberLimit;
    if (this.isMemberLimit) {
      this.newTeamForm.get("memberLimit")?.enable();
    } else {
      this.newTeamForm.get("memberLimit")?.disable();
    }
  }

  onAccept(): void {
    const teamParameters = new EditableTeamParameters(this.newTeamForm.value);
    this.teamClient.createTeam(teamParameters).subscribe(
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
