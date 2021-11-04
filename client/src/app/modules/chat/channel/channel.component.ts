import { Component, ElementRef, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthenticationService } from 'src/app/modules/authentication';
import { User } from '../../authentication/models/user';
import { TextChannel } from '../models/text-channel.model';
import { ChatService } from '../services/chat.service';
import { TextChannelService } from '../services/text-channel.service';

@Component({
  selector: 'channel',
  templateUrl: './channel.component.html',
  styleUrls: ['./channel.component.scss'],
})
export class ChannelComponent implements OnInit, OnDestroy {
  user: User | null;
  allChannels: TextChannel[];
  isSearchOpen: boolean;
  searchedChannels: TextChannel[];
  newChannelName = '';

  isChannelListOpen:boolean;

  @Input () connectedChannels: TextChannel[];

  @ViewChild('addChannelModal', { static: false }) private addChannelModal: ElementRef<HTMLInputElement>;
  @ViewChild('newChannelNameInput', { static: false }) private newChannelNameInput: ElementRef<HTMLInputElement>;
  @ViewChild('searchInput', { static: false }) private searchInput: ElementRef<HTMLInputElement>;

  constructor(
    private authenticationService: AuthenticationService,
    private chatService: ChatService,
    private textChannelService: TextChannelService,
    private snackBar: MatSnackBar,
  ) {
    this.isSearchOpen = false;
    this.connectedChannels = [];
  }

  ngOnInit(): void {
    this.authenticationService.currentUserObservable.subscribe(
      (user) => (this.user = user),
    );

    this.chatService.toggleChannelOverlay.subscribe(()=>{
      this.isChannelListOpen = !this.isChannelListOpen;
      this.toggleChannelOverlay(this.isChannelListOpen);
    });

    this.textChannelService.getChannels().subscribe((channels) => {
      this.allChannels = channels;
      this.searchedChannels = channels;
    });

    this.keyListener();
  }

  ngOnDestroy(): void {}

  addChannel():void {
    const name = this.newChannelName;
    this.newChannelName = '';
    this.newChannelNameInput.nativeElement.value = '';

    const isWhitespace = (name || '').trim().length === 0;
    if (isWhitespace){
      this.snackBar.open('The name can not be empty', 'Close', { duration: 3000 });
      return;
    }
    else{
      let isValid =true;
      if (this.allChannels.find((channel) => channel.name === name)) {
        this.snackBar.open('This channel already exist', 'Close', { duration: 3000 });
        isValid =false;
      }

      if(isValid) {
        this.textChannelService.createChannel(name, this.user?._id as string).subscribe((channel) => {
          this.allChannels.push(channel);
          // should automatically connect user to socket
          this.connectedChannels.push(channel);
          this.chatService.toggleChatOverlay.emit(channel);
       });
      }
    }
  }

  deleteChannel(channelId: string):void {
    this.textChannelService.deleteChannel(channelId).subscribe((channel) => {
      console.log(channel.name + 'has beeen deleted');
    });
  }

  removeChannel(channelId:string):void{

  }

  toggleChannelButton(channel:TextChannel, mouseover:boolean):void{
      const btnDiv = document.getElementById(channel.name) as HTMLInputElement;
      if(mouseover)
        btnDiv.style.display = 'inline';
      else
        btnDiv.style.display = 'none';

      const deletbtn = btnDiv.children.item(1) as HTMLInputElement;
      deletbtn.style.display = 'none';
      if(channel.ownerId == this.user?._id){

        deletbtn.style.display = 'inline';
      }
  }

  searchChannel(evt: Event):void {
    const search = (evt.target as HTMLInputElement).value;
    if (search === null || search.match(/^ *$/) !== null) {
      this.searchedChannels  = Object.assign([], this.allChannels);
    } else {
      this.textChannelService.getChannelsByName(search).subscribe((channels) => {
        this.searchedChannels = channels;
      });
    }
  }

  toggleSearchBar(): void {
    this.isSearchOpen = !this.isSearchOpen;
    if (this.isSearchOpen) {
      this.searchInput.nativeElement.focus();
    }
  }

  openChannel(channel: TextChannel, isSearchOpen: boolean):void {
    const channelOverlay = document.getElementById('channel-overlay') as HTMLInputElement;
    channelOverlay.style.display = 'none';
    this.chatService.toggleChatOverlay.emit(channel);
    this.toggleChannelOverlay(false);
    this.isChannelListOpen = false;

    if (!this.connectedChannels.find((x) => x.name === channel.name)) {
      this.connectedChannels.push(channel);
    }

    if (isSearchOpen) {
      this.searchInput.nativeElement.value = '';
      this.isSearchOpen = false;
    }
  }

  toggleChannelOverlay(open:boolean){
    const channelOverlay = document.getElementById('channel-overlay') as HTMLInputElement;

    if(open) channelOverlay.style.display = 'block';
    else channelOverlay.style.display = 'none';
  }

  openAddChannelModal(){
    const modal = this.addChannelModal.nativeElement;
    modal.style.display = 'block';
  }
  closeAddChannelModal(){
    const modal = this.addChannelModal.nativeElement;
    modal.style.display = 'none';
  }

  onInput(evt: Event): void {
    this.newChannelName = (evt.target as HTMLInputElement).value;
  }

  keyListener() {
    window.addEventListener('keydown', (event) => {
      const modal = this.addChannelModal.nativeElement;
      if (event.key === 'Enter' && modal.style.display !== 'none') {
        this.addChannel();
      }
    });
  }
}
