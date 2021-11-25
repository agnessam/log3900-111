import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { MatDialog, MatDialogRef } from "@angular/material/dialog";
import { ActivatedRoute } from "@angular/router";
import { TeamClientService } from "src/app/modules/backend-communication/team-client/team-client.service";
import { User } from "src/app/modules/users/models/user";
import { Team } from "src/app/shared/models/team.model";
import { ConfirmDeleteDialogComponent } from "../confirm-delete-dialog/confirm-delete-dialog.component";
import { MemberListDialogComponent } from "../member-list-dialog/member-list-dialog.component";

@Component({
  selector: "app-team-profile",
  templateUrl: "./team-profile.component.html",
  styleUrls: ["./team-profile.component.scss"],
})
export class TeamProfileComponent implements OnInit {
  teamId: string;
  team: Team;

  openConfirmDeleteDialogRef: MatDialogRef<ConfirmDeleteDialogComponent>;
  openMemberListDialogRef: MatDialogRef<MemberListDialogComponent>;

  constructor(
    private route: ActivatedRoute,
    private teamClient: TeamClientService,
    private dialog: MatDialog,
    private changeDetectorRef: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.teamId = params["id"];
      this.teamClient.getTeam(this.teamId).subscribe((team) => {
        this.team = team;
      });
      this.teamClient.getTeamDrawings(this.teamId).subscribe((drawings) => {
        this.team.drawings = drawings;
      });
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
    return (this.team.members as User[])
      .map((user) => user._id)
      .includes(userId!);
  }

  isOwner(): boolean {
    const userId = localStorage.getItem("userId");
    return userId == this.team.owner;
  }

  openConfirmDeleteDialog() {
    this.openConfirmDeleteDialogRef = this.dialog.open(
      ConfirmDeleteDialogComponent,
      { data: { team: this.team } }
    );
  }

  openMemberList() {
    this.openMemberListDialogRef = this.dialog.open(MemberListDialogComponent, {
      width: "400px",
      data: { members: this.team.members },
    });
  }
}
