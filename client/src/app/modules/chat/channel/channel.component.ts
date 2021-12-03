import {
  ChangeDetectorRef,
  Component,
  EventEmitter,
  OnDestroy,
  OnInit,
  Output,
} from "@angular/core";
import { MatDialog, MatDialogRef } from "@angular/material/dialog";
import { User } from "../../users/models/user";
import { UsersService } from "../../users/services/users.service";
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
  publicChannels: TextChannel[];
  searchedChannels: TextChannel[];
  connectedChannels: TextChannel[];
  teamChannels: TextChannel[];
  // only one collaboration channel at a time
  collaborationChannel: TextChannel;
  searchQuery: string;
  newChannelDialogRef: MatDialogRef<NewChannelComponent>;

  constructor(
    private chatService: ChatService,
    private chatSocketService: ChatSocketService,
    private textChannelService: TextChannelService,
    private usersService: UsersService,
    private ref: ChangeDetectorRef,
    private dialog: MatDialog
  ) {
    this.connectedChannels = new Array();
    this.publicChannels = new Array();
    this.searchedChannels = new Array();
    this.teamChannels = new Array();
  }

  ngOnInit(): void {
    this.usersService.getUser(localStorage.getItem("userId")!).subscribe({
      next: (user) => {
        this.user = user;
      },
      complete: () => {},
    });
    this.getPublicChannels();
    this.connectTeamChannels();
    this.openDrawingChannel();

    this.textChannelService.newTeamChannel.subscribe((teamChannel) => {
      this.openChannel(teamChannel);
    });

    this.textChannelService.deletedTeamChannel.subscribe(
      (deletedChannelName) => {
        const deletedChannel = this.connectedChannels.filter(
          (connected) => connected.name === deletedChannelName
        );
        if (deletedChannel.length !== 0) this.removeChannel(deletedChannel[0]);
      }
    );
  }

  ngOnDestroy(): void {}

  getPublicChannels(): void {
    this.textChannelService.getChannels().subscribe({
      next: (channels) => {
        channels.forEach((channel) => {
          if (!this.publicChannels.some((item) => item._id === channel._id)) {
            this.publicChannels.push(channel);
          }
        });
        this.resetSearch();
        const general = channels.find((channel) => channel.name === "General");
        if (general) {
          this.chatSocketService.joinRoom({
            userId: this.user?._id!,
            roomName: general.name,
          });
          if (
            this.connectedChannels.some(
              (channel) => channel._id === general._id
            )
          )
            return;
          this.connectedChannels.unshift(general);
        }
      },
    });
  }

  connectTeamChannels(): void {
    this.usersService
      .getUserTeams(localStorage.getItem("userId")!)
      .subscribe((response) => {
        response.forEach((team) => {
          // join team channels automatically
          this.chatSocketService.joinRoom({
            userId: this.user?._id!,
            roomName: team.name,
          });
        });
        this.textChannelService.getTeamChannels().subscribe((channels) => {
          channels.forEach((channel) => {
            const isInConnectedChannels = this.connectedChannels.some(
              (connected) => connected._id === channel._id
            );
            const isUserInTeam = response.some(
              (userTeam) => userTeam._id === channel.team
            );
            if (isUserInTeam && !isInConnectedChannels) {
              this.connectedChannels.push(channel);
            } else if (!isUserInTeam && isInConnectedChannels) {
              this.removeChannel(channel);
            }
          });
        });
      });
  }

  openDrawingChannel(): void {
    this.textChannelService.joinedCollabChannel.subscribe((channel) => {
      this.openChannel(channel);
    });

    this.textChannelService.leftCollabChannel.subscribe((channel) => {
      this.resetSearch();
      this.removeChannel(channel);
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
    const indexAll = this.publicChannels.findIndex(
      (x) => x._id === channel._id
    );
    this.publicChannels.splice(indexAll, 1);

    this.chatService.leaveRoomEventEmitter.emit(channel);

    this.closeChannelList.emit();
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
    if (
      this.searchQuery === undefined ||
      this.searchQuery === null ||
      this.searchQuery?.match(/^ *$/) !== null
    ) {
      this.searchedChannels = Object.assign([], this.publicChannels);
    } else {
      this.textChannelService
        .searchChannels(this.searchQuery)
        .subscribe((channels) => {
          this.searchedChannels = channels;
        });
    }
  }

  resetSearch(): void {
    if (
      this.searchQuery === undefined ||
      this.searchQuery === null ||
      this.searchQuery?.match(/^ *$/) !== null
    ) {
      this.searchedChannels = Object.assign([], this.publicChannels);
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

      this.publicChannels.push(channel);
      this.searchedChannels = this.publicChannels;
      this.closeChannelList.emit();
      // should automatically connect user to socket
      this.connectedChannels.push(channel);
      this.chatService.toggleChatOverlay.emit(channel);
      this.ref.detectChanges();
    });
  }

  isOwner(channelOwner: string) {
    return channelOwner == this.user?._id;
  }
}
