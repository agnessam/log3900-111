import { Router } from "@angular/router";
import { Component, OnInit } from "@angular/core";
import { TeamClientService } from "src/app/modules/backend-communication/team-client/team-client.service";

@Component({
  selector: "app-team-main-page",
  templateUrl: "./team-main-page.component.html",
  styleUrls: ["./team-main-page.component.scss"],
})
export class TeamMainPageComponent implements OnInit {
  teams: Set<any> = new Set();

  constructor(private teamClient: TeamClientService, private router: Router) {}

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
}
