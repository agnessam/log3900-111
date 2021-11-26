import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { MatDialog, MatDialogRef } from "@angular/material/dialog";
import { Router } from "@angular/router";
import { TeamClientService } from "src/app/modules/backend-communication/team-client/team-client.service";
import { Team } from "src/app/shared/models/team.model";
import { NewTeamComponent } from "../../new-team/new-team.component";

@Component({
  selector: "app-team-main-page",
  templateUrl: "./team-main-page.component.html",
  styleUrls: ["./team-main-page.component.scss"],
})
export class TeamMainPageComponent implements OnInit {
  gridColumns: number = 3;
  teams: Array<Team> = new Array();

  newTeamDialogRef: MatDialogRef<NewTeamComponent>;

  constructor(
    private teamClient: TeamClientService,
    private router: Router,
    private dialog: MatDialog,
    private changeDetectorRef: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.teamClient.getTeams().subscribe((response) => {
      response.forEach((team) => {
        this.teams.push(team);
      });
    });
  }

  ngAfterViewInit(): void {}

  goToTeam(teamId: string): void {
    this.router.navigate([`/teams/${teamId}`]);
  }

  openCreateTeamDialog(): void {
    this.newTeamDialogRef = this.dialog.open(NewTeamComponent, {
      width: "400px",
      height: "350px",
    });
    this.newTeamDialogRef.afterClosed().subscribe((result) => {
      if (!result) {
        return;
      }

      this.teams.push(result);
    });
  }

  isAlreadyJoined(team: Team): boolean {
    const userId = localStorage.getItem("userId");
    return team.members.includes(userId!);
  }

  isTeamFull(team: Team): boolean {
    if (!team.memberLimit) return false;
    if (team.members.length >= team.memberLimit) {
      return true;
    }
    return false;
  }

  joinTeam(teamId: string) {
    return this.teamClient.joinTeam(teamId).subscribe((team) => {
      for (let i = 0; i < this.teams.length; ++i) {
        if (this.teams[i]._id == teamId) {
          this.teams[i] = team;
        }
      }
      this.changeDetectorRef.detectChanges();
      return team;
    });
  }
}
