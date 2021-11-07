import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { TeamClientService } from "src/app/modules/backend-communication/team-client/team-client.service";

@Component({
  selector: "app-team-profile",
  templateUrl: "./team-profile.component.html",
  styleUrls: ["./team-profile.component.scss"],
})
export class TeamProfileComponent implements OnInit {
  teamId: string;
  team: any;

  constructor(
    private route: ActivatedRoute,
    private teamClient: TeamClientService
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.teamId = params["id"];
      this.teamClient.getTeam(this.teamId).subscribe((team) => {
        this.team = team;
      });
    });
  }
}
