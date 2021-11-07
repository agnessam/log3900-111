import { Component, OnInit } from "@angular/core";
import { MatDialog } from "@angular/material/dialog";
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

  constructor(
    private teamClient: TeamClientService,
    private router: Router,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {}

  ngAfterViewInit(): void {
    this.teamClient.getTeams().subscribe((response) => {
      response.forEach((team) => {
        this.teams.add(team);
      });
    });
  }

  goToTeam(teamId: string): void {
    this.router.navigate([`/teams/${teamId}`]);
  }

  openCreateTeamDialog(): void {
    this.dialog.open(NewTeamComponent, {});
  }
}
