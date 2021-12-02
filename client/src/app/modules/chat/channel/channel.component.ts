import {
  ChangeDetectorRef,
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
} from "@angular/core";
import { MatDialog, MatDialogRef } from "@angular/material/dialog";
import { AuthenticationService } from "src/app/modules/authentication";
import { Team } from "src/app/shared/models/team.model";
import { User } from "../../authentication/models/user";
import { TeamClientService } from "../../backend-communication/team-client/team-client.service";
import { TextChannel } from "../models/text-channel.model";
import { ChatSocketService } from "../services/chat-socket.service";
import { ChatService } from "../services/chat.service";
import { TextChannelService } from "../services/text-channel.service";
import { NewChannelComponent } from "./new-channel/new-channel.component";

@Component({
  selector: "channel",
  templateUrl: "./channel.component.html",
  styleUrls: ["./channel.component.scss"],
})
export class ChannelComponent implements OnInit, OnDestroy {
  @Output() closeChannelList = new EventEmitter<any>();
  user: User | null;
  allTeams: Team[];
  allChannels: TextChannel[];
  searchedChannels: TextChannel[];
  connectedChannels: TextChannel[];
  teamChannels: TextChannel[];
  // only one collaboration channel at a time
  collaborationChannel : TextChannel;
  searchQuery: string;
  newChannelDialogRef: MatDialogRef<NewChannelComponent>;
  currentDrawingId: string | undefined;

  constructor(
    private authenticationService: AuthenticationService,
    private chatService: ChatService,
    private chatSocketService: ChatSocketService,
    private textChannelService: TextChannelService,
    private teamClient: TeamClientService,
    private ref: ChangeDetectorRef,
    private dialog: MatDialog,
  ) {
    this.connectedChannels = new Array();
    this.allChannels = new Array();
    this.searchedChannels = new Array();
    this.allTeams = new Array();
    this.teamChannels = new Array();
    this.currentDrawingId = undefined;
  }

  ngOnInit(): void {
    this.authenticationService.currentUserObservable.subscribe(
      (user) => (this.user = user)
    );

    this.textChannelService.getChannels().subscribe({
      next: (channels) => {
        channels.forEach(channel => this.allChannels.push(Object.assign({}, channel)));
        this.resetSearch();
        const general = channels.find((channel) => channel.name === "General");
        if (general) {
          this.chatSocketService.joinRoom({
            userId: this.user!._id,
            roomName: general.name,
          });
          this.connectedChannels.unshift(general);
        }
        this.ref.detectChanges();
      },
      complete: () => {
        this.filterTeamChannels();
        this.filterCollaborationChannels();
        this.ref.detectChanges();
      },
    });

    this.textChannelService.newTeamChannel.subscribe((teamChannel) => {
      this.allChannels.push(teamChannel);
      this.resetSearch();
      this.openChannel(teamChannel);
    });

  }

  ngOnDestroy(): void {}

  // only get channels that are visible to the user: teams and collaboration channels
  filterTeamChannels(): void {
    this.teamClient.getTeams().subscribe((response) => {
      response.forEach((team) => {
        this.allTeams.push(team);
        const index = this.allChannels.findIndex((channel) => channel.name === team.name );
        if (index !== -1) {
          // remove from list if user is not a member
          if (!(team.members as string[]).includes(this.user!._id) ) {
              this.allChannels.splice(index, 1);
              const connectedIndex = this.connectedChannels.findIndex((channel) => channel.name === team.name);
              if (connectedIndex !== -1)
                this.connectedChannels.splice(connectedIndex, 1);
              this.resetSearch();
          } else {
            // join team channels automatically
            this.chatSocketService.joinRoom({userId: this.user!._id, roomName: team.name});
            this.connectedChannels.push(this.allChannels[index]);
          }
        }
      });
    });
  }

  filterCollaborationChannels(): void {
    // purge collab channel from list in case we're not in one
    if (this.currentDrawingId === undefined) {
      this.allChannels.splice(this.allChannels.findIndex((channel) => channel.drawing !== undefined), 1);
    }

    this.textChannelService.joinedCollabChannel.subscribe((channel) => {
      this.currentDrawingId = channel.drawing;
      if (!this.allChannels.includes(channel)) {
        this.allChannels.push(channel);
      }
      this.connectedChannels.push(channel);
      this.resetSearch();
      this.openChannel(channel);
    });

    this.textChannelService.leftCollabChannel.subscribe((channel) => {
      this.currentDrawingId = undefined;
      if (this.allChannels.includes(channel)) {
        this.allChannels.splice(this.allChannels.indexOf(channel), 1);
      }
      this.connectedChannels.splice(this.connectedChannels.indexOf(channel), 1);
      this.resetSearch();

    });
  }

  // can only delete channel if owner id corresponds to user id
  deleteChannel(channel: TextChannel): void {
    this.textChannelService.deleteChannel(channel._id).subscribe(() => {
      console.log(channel.name + " has been deleted");
    });

    this.textChannelService.deleteMessages(channel._id).subscribe(() => {
      console.log("messages from " + channel.name + " have been deleted");
    });

    const indexConnected = this.connectedChannels.findIndex(
      (x) => x._id === channel._id
    );
    this.connectedChannels.splice(indexConnected, 1);
    const indexAll = this.allChannels.findIndex((x) => x._id === channel._id);
    this.allChannels.splice(indexAll, 1);

    this.chatService.leaveRoomEventEmitter.emit(channel);
  }

  removeChannel(channel: TextChannel): void {
    const index = this.connectedChannels.findIndex(
      (x) => x._id === channel._id
    );
    this.connectedChannels.splice(index, 1);
    // leaves room in chat socket service to better handle connected channels
    this.chatService.leaveRoomEventEmitter.emit(channel);
  }

  toggleChannelButton(channel: TextChannel, mouseover: boolean): void {
    if (channel.name !== "General") {
      const btnDiv = document.getElementById(channel.name) as HTMLInputElement;
      if (mouseover) btnDiv.style.display = "inline";
      else btnDiv.style.display = "none";

      const deletbtn = btnDiv.children.item(1) as HTMLInputElement;
      deletbtn.style.display = "none";
      if (channel.ownerId === this.user?._id) {
        deletbtn.style.display = "inline";
      }
    }
  }

  searchChannels(): void {
    if (this.searchQuery === undefined || this.searchQuery === null  || this.searchQuery?.match(/^ *$/) !== null) {
      this.searchedChannels = Object.assign([], this.allChannels);
    } else {
      this.textChannelService
        .searchChannels(this.searchQuery)
        .subscribe((channels) => {
          this.allTeams.forEach((team) => {
            const index = channels.findIndex((channel) => channel.name === team.name );
            if (index !== -1 && !(team.members as string[]).includes(this.user!._id)) {
              channels.splice(index, 1);
            }
            this.searchedChannels = channels;
          });
        });
    }
  }

  resetSearch(): void {
    if (this.searchQuery === undefined || this.searchQuery === null  || this.searchQuery?.match(/^ *$/) !== null) {
      this.searchedChannels = Object.assign([], this.allChannels);
    }
  }

  openChannel(channel: TextChannel): void {
    this.chatService.toggleChatOverlay.emit(channel);
    this.closeChannelList.emit();

    if (!this.connectedChannels.find((x) => x.name === channel.name)) {
      this.connectedChannels.push(channel);
    }

    this.searchQuery = "";
  }

  openAddChannelModal() {
    this.newChannelDialogRef = this.dialog.open(NewChannelComponent, {});
    this.newChannelDialogRef.afterClosed().subscribe((channel) => {
      if (!channel) {
        return;
      }

      this.allChannels.push(channel);
      this.searchedChannels = this.allChannels;
      this.closeChannelList.emit();
      // should automatically connect user to socket
      this.connectedChannels.push(channel);
      this.chatService.toggleChatOverlay.emit(channel);
      this.ref.detectChanges();
    });
  }
}
