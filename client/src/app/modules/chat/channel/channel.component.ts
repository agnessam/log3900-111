import { Component, OnDestroy, OnInit, } from "@angular/core";
import { AuthenticationService } from "src/app/modules/authentication";
import { User } from "../../authentication/models/user";
import { TextChannel } from '../models/text-channel.model';
import { ChatService } from "../services/chat.service";
import { TextChannelService } from '../services/text-channel.service';

@Component({
  selector: 'channel',
  templateUrl: './channel.component.html',
  styleUrls: ['./channel.component.scss'],
})
export class ChannelComponent implements OnInit, OnDestroy {
  user: User | null;
  channels: TextChannel[];
  isSearchOpen: boolean;

  channelOverlayStatus:boolean;

  constructor(
    private authenticationService: AuthenticationService,
    private chatService:ChatService,
    private textChannelService: TextChannelService,
  ) {
    this.authenticationService.currentUserObservable.subscribe(
      (user) => (this.user = user),
    );
    this.isSearchOpen = false;
  }

  ngOnInit(): void {
    this.chatService.toggleChannelOverlay.subscribe((status)=>{
      this.channelOverlayStatus = status;
      this.toggleChannelOverlay(status);
    });
    this.textChannelService.getChannels().subscribe((channels) => {
      this.channels = channels;
      console.log(channels);
    });
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
    console.log(search);
  }

  toggleSearchBar(): void {
    this.isSearchOpen = !this.isSearchOpen;
    console.log(this.isSearchOpen);
  }

  openChannel(channel: TextChannel):void {
    const channelOverlay = document.getElementById('channel-overlay') as HTMLInputElement;
    channelOverlay.style.display = 'none';
    this.chatService.toggleChatOverlay.emit(channel);
    this.toggleChannelOverlay(true);
    this.channelOverlayStatus = false;
  }

  toggleChannelOverlay(isOpen:boolean){
    const channelOverlay = document.getElementById('channel-overlay') as HTMLInputElement;

    if(isOpen) channelOverlay.style.display = 'none';
    else channelOverlay.style.display = 'block';
  }
}
