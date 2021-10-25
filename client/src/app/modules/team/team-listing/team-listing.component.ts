import { Component, OnInit } from "@angular/core";
import { Router } from "@angular/router";
import { TeamService } from "../services/team.service";
import { MatDialog } from "@angular/material/dialog";
import { TeamCreationComponent } from "../team-creation/team-creation.component";

@Component({
  selector: "app-team-listing",
  templateUrl: "./team-listing.component.html",
  styleUrls: ["./team-listing.component.scss"],
})
export class TeamListingComponent implements OnInit {
  teamNameIdPair: { [teamName: string]: string } = {};
  teamNames: string[] = [];
  constructor(
    private teamService: TeamService,
    private router: Router,
    public dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.teamService.getTeams().subscribe((teams) => {
      for (let team of teams) {
        if (team.name && team._id) {
          this.teamNameIdPair[team.name] = team._id;
          this.teamNames.push(team.name);
        }
      }
    });
  }

  joinTeam(teamName: string): void {
    console.log("Join now: " + teamName);
    let teamId = this.teamNameIdPair[teamName];
    this.teamService.getTeam(teamId).subscribe((response) => {
      console.log(response);
      this.router.navigate([""]);
    });
  }

  createTeam(): void {
    console.log("create team");
    this.openDialog();
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(TeamCreationComponent, {
      width: "75%",
      // data: {name: this.name, animal: this.animal}
    });

    dialogRef.afterClosed().subscribe((result) => {
      // this.teamService.createTeam(team)
      console.log("REf closed");
    });
  }
}
