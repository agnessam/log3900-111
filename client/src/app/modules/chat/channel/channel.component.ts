import {
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild,
  ViewEncapsulation,
} from "@angular/core";
import { MatSnackBar } from "@angular/material/snack-bar";
import { AuthenticationService } from "src/app/modules/authentication";
import { User } from "../../authentication/models/user";
import { TextChannel } from "../models/text-channel.model";
import { ChatSocketService } from "../services/chat-socket.service";
import { ChatService } from "../services/chat.service";
import { TextChannelService } from "../services/text-channel.service";

@Component({
  selector: "channel",
  templateUrl: "./channel.component.html",
  styleUrls: ["./channel.component.scss"],
})
export class ChannelComponent implements OnInit, OnDestroy {
  encapsulation: ViewEncapsulation.None
  user: User | null;
  allChannels: TextChannel[];
  isSearchOpen: boolean;
  searchedChannels: TextChannel[];
  newChannelName: string;
  isChannelListOpen: boolean;
  connectedChannels: TextChannel[];
  searchQuery: string;

  @ViewChild("addChannelModal", { static: false })
  private addChannelModal: ElementRef<HTMLInputElement>;

  constructor(
    private authenticationService: AuthenticationService,
    private chatService: ChatService,
    private chatSocketService: ChatSocketService,
    private textChannelService: TextChannelService,
    private snackBar: MatSnackBar
  ) {
    this.isSearchOpen = false;
    this.connectedChannels = [];
  }

  ngOnInit(): void {
    this.authenticationService.currentUserObservable.subscribe(
      (user) => (this.user = user)
    );

    this.chatService.toggleChannelOverlay.subscribe(() => {
      this.isChannelListOpen = !this.isChannelListOpen;
      this.toggleChannelOverlay(this.isChannelListOpen);
    });

    this.textChannelService.getChannels().subscribe((channels) => {
      this.allChannels = channels;
      this.searchedChannels = channels;
      const general = channels.find((channel) => channel.name === "General");
      if (general) {
        this.chatSocketService.joinRoom(general.name);
        this.connectedChannels.unshift(general);
      }
    });

    // this.keyListener();
  }

  ngOnDestroy(): void {}

  addChannel(): void {
    const name = this.newChannelName;
    this.newChannelName = "";

    const isWhitespace = (name || "").trim().length === 0;
    if (isWhitespace) {
      this.snackBar.open("The name can not be empty", "Close", {
        duration: 3000,
      });
      return;
    } else {
      let isValid = true;
      if (this.allChannels.find((channel) => channel.name === name)) {
        this.snackBar.open("This channel already exists", "Close", {
          duration: 3000,
        });
        isValid = false;
      }

      if (isValid) {
        this.textChannelService
          .createChannel(name, this.user?._id as string)
          .subscribe((channel) => {
            this.allChannels.push(channel);
            // should automatically connect user to socket
            this.connectedChannels.push(channel);
            this.chatService.toggleChatOverlay.emit(channel);
            this.isSearchOpen = false;
            this.toggleChannelOverlay(false);
            this.closeAddChannelModal();
          });
      }
    }
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
          this.searchedChannels = channels;
        });
    }
  }

  toggleSearch(isOpen: boolean): void {
    this.isSearchOpen = isOpen;
    if (this.searchQuery === undefined || this.searchQuery === null  || this.searchQuery?.match(/^ *$/) !== null) {
      this.searchedChannels = Object.assign([], this.allChannels);
    }

    // if (this.isSearchOpen) {
    //   setTimeout(() => {
    //     this.searchInput.nativeElement.focus();
    //   }, 0);
    // }
  }

  openChannel(channel: TextChannel): void {
    this.chatService.toggleChatOverlay.emit(channel);
    this.toggleChannelOverlay(false);
    this.isChannelListOpen = false;

    if (!this.connectedChannels.find((x) => x.name === channel.name)) {
      this.connectedChannels.push(channel);
    }

    if (this.isSearchOpen) {
      this.searchQuery = "";
      this.isSearchOpen = false;
    }
  }

  toggleChannelOverlay(open: boolean) {
    const channelOverlay = document.getElementById(
      "channel-overlay"
    ) as HTMLInputElement;

    if (open) channelOverlay.style.display = "block";
    else channelOverlay.style.display = "none";
  }

  openAddChannelModal() {
    const modal = this.addChannelModal.nativeElement;
    modal.style.display = "block";
  }
  closeAddChannelModal() {
    const modal = this.addChannelModal.nativeElement;
    modal.style.display = "none";
  }
}
