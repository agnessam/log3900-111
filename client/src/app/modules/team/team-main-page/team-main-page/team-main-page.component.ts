import { Component, OnInit } from "@angular/core";
import { MatDialog, MatDialogRef } from "@angular/material/dialog";
import { Router } from "@angular/router";
import { TeamClientService } from "src/app/modules/backend-communication/team-client/team-client.service";
import { NewTeamComponent } from "../../new-team/new-team.component";

@Component({
  selector: "app-team-main-page",
  templateUrl: "./team-main-page.component.html",
  styleUrls: ["./team-main-page.component.scss"],
})
export class TeamMainPageComponent implements OnInit {
  gridColumns: number = 3;
  teams: Set<any> = new Set();

  newTeamDialogRef: MatDialogRef<NewTeamComponent>;

  constructor(
    private teamClient: TeamClientService,
    private router: Router,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.teamClient.getTeams().subscribe((response) => {
      response.forEach((team) => {
        this.teams.add(team);
      });
    });
  }

  ngAfterViewInit(): void {}

  goToTeam(teamId: string): void {
    this.router.navigate([`/teams/${teamId}`]);
  }

  openCreateTeamDialog(): void {
    this.newTeamDialogRef = this.dialog.open(NewTeamComponent, {});
    this.newTeamDialogRef
      .afterClosed()
      .subscribe((result) => this.teams.add(result));
  }
}
