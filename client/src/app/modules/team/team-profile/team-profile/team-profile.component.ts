import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { TeamClientService } from "src/app/modules/backend-communication/team-client/team-client.service";
import { Team } from "src/app/shared/models/team.model";

@Component({
  selector: "app-team-profile",
  templateUrl: "./team-profile.component.html",
  styleUrls: ["./team-profile.component.scss"],
})
export class TeamProfileComponent implements OnInit {
  teamId: string;
  team: Team;

  constructor(
    private route: ActivatedRoute,
    private teamClient: TeamClientService,
    private changeDetectorRef: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.teamId = params["id"];
      this.teamClient.getTeam(this.teamId).subscribe((team) => {
        console.log(this.team);
        this.team = team;
      });
    });
  }

  ngAfterViewInit(): void {
    this.teamClient.getTeam(this.teamId).subscribe((team) => {
      console.log(this.team);
      this.team = team;
    });
  }

  joinTeam(teamId: string) {
    this.teamClient.joinTeam(teamId).subscribe((team) => {
      this.team = team;
      this.changeDetectorRef.detectChanges();
    });
  }

  isAlreadyJoined(): boolean {
    const userId = localStorage.getItem("userId");
    return this.team.members.includes(userId!);
  }
}