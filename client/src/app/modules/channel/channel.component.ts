import { Component, OnDestroy, OnInit, } from "@angular/core";
import { Subscription } from "rxjs";
import { AuthenticationService } from "src/app/modules/authentication";
import { User } from "../authentication/models/user";
import { ChatService } from "../chat/services/chat.service";
import { TextChannel } from '../chat/models/text-channel.model';
import { TextChannelService } from '../chat/services/text-channel.service';

@Component({
  selector: "channel",
  templateUrl: "./channel.component.html",
  styleUrls: ["./channel.component.scss"],
})
export class ChannelComponent implements OnInit, OnDestroy {
  public user: User | null;
  public chatSubscription: Subscription;
  public channels: TextChannel[];
  public isSearchOpen: boolean;

  public channelOverlayStatus:boolean;

  //@Input() private chatRoomName: string = "default";

  constructor(
    private authenticationService: AuthenticationService,
    private chatService:ChatService,
    private textChannelService: TextChannelService,
  ) {
    this.authenticationService.currentUserObservable.subscribe(
      (user) => (this.user = user)
    );
    this.isSearchOpen = false;
  }

  ngOnInit(): void {
    this.chatService.toggleChannelOverlay.subscribe(status=>{
      this.channelOverlayStatus = status;
      this.toggleChannelOverlay(status);
    });
    this.textChannelService.getChannels().subscribe((channels) => {
      this.channels = channels;
      console.log(channels);
    })
  }

  ngOnDestroy(): void {
  }

  addChannel():void {
  // this.textChannelService.createChannel('testname', this.user?._id as string).subscribe((channel) => {
  //   console.log(channel);
  // });
  }

  searchChannel(evt: Event):void {
    const search = (evt.target as HTMLInputElement).value;
    console.log(search)
  }

  toggleSearchBar(): void {
    this.isSearchOpen = !this.isSearchOpen;
    console.log(this.isSearchOpen);
  }

  openChannel(channel: TextChannel):void {
    let channelOverlay = <HTMLInputElement>document.getElementById("channel-overlay");
    channelOverlay.style.display = "none";
    this.chatService.toggleChatOverlay.emit(channel);
    this.toggleChannelOverlay(true);
    this.channelOverlayStatus = false;
  }

  toggleChannelOverlay(isOpen:boolean){
    let channelOverlay = <HTMLInputElement>document.getElementById("channel-overlay");

    if(isOpen) channelOverlay.style.display = "none";
    else channelOverlay.style.display = "block";
  }
}
