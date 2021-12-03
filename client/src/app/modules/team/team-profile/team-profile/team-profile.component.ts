import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { MatDialog, MatDialogRef } from "@angular/material/dialog";
import { ActivatedRoute } from "@angular/router";
import { TeamClientService } from "src/app/modules/backend-communication/team-client/team-client.service";
import { ChatSocketService } from "src/app/modules/chat/services/chat-socket.service";
import { TextChannelService } from "src/app/modules/chat/services/text-channel.service";
import { User } from "src/app/modules/users/models/user";
import { Drawing } from "src/app/shared";
import { Team } from "src/app/shared/models/team.model";
import { ConfirmDeleteDialogComponent } from "../confirm-delete-dialog/confirm-delete-dialog.component";
import { ConfirmJoinDialogComponent } from "../confirm-join-dialog/confirm-join-dialog.component";
import { ConfirmLeaveDialogComponent } from "../confirm-leave-dialog/confirm-leave-dialog.component";
import { EditTeamParametersComponent } from "../edit-team-parameters/edit-team-parameters.component";
import { MemberListDialogComponent } from "../member-list-dialog/member-list-dialog.component";

@Component({
  selector: "app-team-profile",
  templateUrl: "./team-profile.component.html",
  styleUrls: ["./team-profile.component.scss"],
})
export class TeamProfileComponent implements OnInit {
  teamId: string;
  team: Team;

  teamLoaded: Promise<boolean>;

  openConfirmDeleteDialogRef: MatDialogRef<ConfirmDeleteDialogComponent>;
  openConfirmJoinDialogRef: MatDialogRef<ConfirmJoinDialogComponent>;
  openConfirmLeaveDialogRef: MatDialogRef<ConfirmLeaveDialogComponent>;
  openMemberListDialogRef: MatDialogRef<MemberListDialogComponent>;
  openEditTeamParametersDialogRef: MatDialogRef<EditTeamParametersComponent>;

  constructor(
    private route: ActivatedRoute,
    private teamClient: TeamClientService,
    private textChannelService: TextChannelService,
    private chatSocketService: ChatSocketService,
    private dialog: MatDialog,
    private changeDetectorRef: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe((params) => {
      this.teamId = params["id"];
      this.teamClient.getTeam(this.teamId).subscribe((team) => {
        this.team = team;
        this.teamLoaded = Promise.resolve(true);
      });
    });
  }

  joinTeam(teamId: string) {
    this.teamClient.joinTeam(teamId).subscribe((team) => {
      this.team = team;
      this.chatSocketService.joinRoom({
        userId: localStorage.getItem("userId")!,
        roomName: team.name,
      });
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

  isTeamFull(): boolean {
    if (!this.team.memberLimit) {
      return false;
    }
    if (this.team.members.length >= this.team.memberLimit) {
      return true;
    }
    return false;
  }

  openConfirmJoinDialog() {
    this.openConfirmJoinDialogRef = this.dialog.open(
      ConfirmJoinDialogComponent,
      { data: { team: this.team } }
    );
    this.openConfirmJoinDialogRef.afterClosed().subscribe((team) => {
      if (!team) return;
      this.team = team;
      this.changeDetectorRef.detectChanges();
    });
  }

  openConfirmDeleteDialog() {
    this.openConfirmDeleteDialogRef = this.dialog.open(
      ConfirmDeleteDialogComponent,
      { data: { team: this.team } }
    );

    this.openConfirmDeleteDialogRef.afterClosed().subscribe(() => {
      this.textChannelService.emitDeleteTeamChannel(this.team.name);
    })
  }

  openConfirmLeaveDialog() {
    this.openConfirmLeaveDialogRef = this.dialog.open(
      ConfirmLeaveDialogComponent,
      { data: { team: this.team } }
    );
    this.openConfirmLeaveDialogRef.afterClosed().subscribe((team) => {
      if (!team) {
        return;
      }
      this.team = team;
      this.chatSocketService.leaveRoom(team.name);
      this.changeDetectorRef.detectChanges();
    });
  }

  openEditTeamParametersDialog() {
    this.openEditTeamParametersDialogRef = this.dialog.open(
      EditTeamParametersComponent,
      { data: this.team }
    );
    this.openEditTeamParametersDialogRef
      .afterClosed()
      .subscribe((updatedTeam: Team) => {
        if (updatedTeam) {
          this.team.description = updatedTeam.description;
          this.team.isPrivate = updatedTeam.isPrivate;
          this.team.password = updatedTeam.password;
        }
      });
  }

  openMemberList() {
    this.openMemberListDialogRef = this.dialog.open(MemberListDialogComponent, {
      width: "400px",
      data: { members: this.team.members },
    });
  }

  deleteDrawingFromView(deletedDrawing: Drawing) {
    this.team.drawings.splice(
      (this.team.drawings as Drawing[]).indexOf(deletedDrawing),
      1
    );
    this.changeDetectorRef.detectChanges();
  }
}
