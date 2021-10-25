import { Component, OnInit } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { MatDialogRef } from "@angular/material/dialog";
import { TeamService } from "../services/team.service";
@Component({
  selector: "app-team-creation",
  templateUrl: "./team-creation.component.html",
  styleUrls: ["./team-creation.component.scss"],
})
export class TeamCreationComponent implements OnInit {
  teamCreationForm: FormGroup;
  name: string;
  description: string;
  constructor(
    public dialogRef: MatDialogRef<TeamCreationComponent>,
    private teamService: TeamService
  ) {}

  ngOnInit(): void {
    this.teamCreationForm = new FormGroup({});
  }

  createTeam(): void {
    let newTeam = {
      name: this.name,
      description: this.description,

      owner: "BOB",
      members: [],

      drawings: [],
    };
    console.log(newTeam);
    this.teamService.createTeam(newTeam);
  }
}
