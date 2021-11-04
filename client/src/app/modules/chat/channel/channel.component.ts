import { Component, OnDestroy, OnInit, ViewChild, ElementRef } from "@angular/core";
import { AuthenticationService } from "src/app/modules/authentication";
import { User } from "../../authentication/models/user";
import { TextChannel } from '../models/text-channel.model';
import { ChatService } from "../services/chat.service";
import { TextChannelService } from '../services/text-channel.service';
import { MatSnackBar } from "@angular/material/snack-bar";

@Component({
  selector: 'channel',
  templateUrl: './channel.component.html',
  styleUrls: ['./channel.component.scss'],
})
export class ChannelComponent implements OnInit, OnDestroy {
  user: User | null;
  channels: TextChannel[];
  isSearchOpen: boolean;
  newChannelName: string = '';

  public isChannelListOpen:boolean;

  @ViewChild('addChannelModal', { static: false }) private addChannelModal: ElementRef<HTMLInputElement>;
  @ViewChild('newChannelNameInput', { static: false }) private newChannelNameInput: ElementRef<HTMLInputElement>;

  //@Input() private chatRoomName: string = "default";

  constructor(
    private authenticationService: AuthenticationService,
    private chatService:ChatService,
    private textChannelService: TextChannelService,
    private snackBar: MatSnackBar,
  ) {
    this.authenticationService.currentUserObservable.subscribe(
      (user) => (this.user = user),
    );
    this.isSearchOpen = false;
  }

  ngOnInit(): void {
    this.chatService.toggleChannelOverlay.subscribe(status=>{
      this.isChannelListOpen = !this.isChannelListOpen;
      this.toggleChannelOverlay(this.isChannelListOpen);
    });
    this.textChannelService.getChannels().subscribe((channels) => {
      this.channels = channels;
    });
    this.keyListener();
  }

  ngOnDestroy(): void {
  }

  addChannel():void {
    let name=this.newChannelName;
    this.newChannelName = '';
    this.newChannelNameInput.nativeElement.value = '';

    let isWhitespace = (name || '').trim().length === 0;
    if (isWhitespace){
      this.snackBar.open("The name can not be empty", "Close", { duration: 3000 });
      return;
    }
    else{
      let isValid =true;
      this.channels.forEach(channel => {
        if(channel.name == name){
          this.snackBar.open("This channel already exist", "Close", { duration: 3000 });
          isValid =false;
        }
      });

      if(isValid) {
        this.textChannelService.createChannel(name, this.user?._id as string).subscribe((channel) => {
          console.log(channel);
       });
      }
    }
  }

  deleteChannel(channelId: string):void {
    this.textChannelService.deleteChannel(channelId).subscribe((channel) => {
      console.log(channel.name + "has beeen delete");
    });
  }

  removeChannel(channelId:string):void{

  }

  toggleChannelButton(channelId:string, mouseover:boolean):void{
      const btnDiv = document.getElementById(channelId) as HTMLInputElement;
      if(mouseover)
        btnDiv.style.display = "inline";
      else
        btnDiv.style.display = "none";
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
    this.toggleChannelOverlay(false);
    this.isChannelListOpen = false;
  }

  toggleChannelOverlay(open:boolean){
    let channelOverlay = <HTMLInputElement>document.getElementById("channel-overlay");

    if(open) channelOverlay.style.display = "block";
    else channelOverlay.style.display = "none";
  }

  openAddChannelModal(){
    const modal = this.addChannelModal.nativeElement;
    modal.style.display = "block";
  }
  closeAddChannelModal(){
    const modal = this.addChannelModal.nativeElement;
    modal.style.display = "none";
  }

  onInput(evt: Event): void {
    this.newChannelName = (evt.target as HTMLInputElement).value;
  }

  keyListener() {
    window.addEventListener('keydown', (event) => {
      const modal = this.addChannelModal.nativeElement;
      if (event.key === 'Enter' && modal.style.display != "none") {
        this.addChannel();
      }
    });
  }
}
