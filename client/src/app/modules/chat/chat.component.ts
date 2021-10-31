import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs';
import { AuthenticationService } from 'src/app/modules/authentication';
import { User } from '../authentication/models/user';
import { ChatSocketService } from './services/chat-socket.service';
import { ChatService } from './services/chat.service';
// import { TextChannelService } from './services/text-channel.service';

@Component({
  selector: 'chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss'],
})
export class ChatComponent implements OnInit, OnDestroy {
  user: User | null;
  chatSubscription: Subscription;
  @ViewChild('messageBody', { static: false }) private messageBody: ElementRef<HTMLInputElement>;
  @ViewChild('chatBox', { static: false }) private chatBox: ElementRef<HTMLInputElement>;
  @ViewChild('arrowDown', { static: false }) private iconArrowDown: ElementRef<HTMLInputElement>;
  @ViewChild('arrowUp', { static: false }) private iconArrowUp: ElementRef<HTMLInputElement>;
  @ViewChild('chatBottom', { static: false }) private chatBottom: ElementRef<HTMLInputElement>;
  message = '';
  // messageHistory: Message[] = [];
  channels: string[] = ['hello', 'world', 'patate'];
  chatStatus:boolean;
  isMinimized = false;
  isPopoutOpen = false;

  chatRoomName = 'default';

  constructor(
    private authenticationService: AuthenticationService,
    private chatSocketService: ChatSocketService,
    private chatService: ChatService,
    // private textChannelService: TextChannelService,
  ) {
    this.authenticationService.currentUserObservable.subscribe(
      (user) => (this.user = user),
    );
    this.chatService.toggleChatOverlay.subscribe(channel=>{
      this.chatRoomName = channel.name;
      this.openChat(this.chatRoomName);
    });
  }

  ngOnInit(): void {
    this.keyListener();
    this.receiveMessage();
    this.chatSocketService.connect();
    this.chatSocketService.joinRoom(this.chatRoomName);
  }

  ngOnDestroy(): void {
    // this.textChannelService.saveMessages(message);
    this.chatSocketService.leaveRoom(this.chatRoomName);
    this.chatSocketService.disconnect();
    this.chatSubscription.unsubscribe();
  }

  // Note: this event listener listens throughout the whole app
  keyListener() {
    window.addEventListener('keydown', (event) => {
      if (event.key === 'Enter') {
        this.sendMessage();
      }
    });
  }

  receiveMessage() {
    this.chatSubscription = this.chatSocketService.chatSubject.subscribe({
      next: (message) => {
        console.log(message)
        console.log("message reveicved")
        const messageBox = this.chatBox.nativeElement;
        const p = document.createElement('div');
        p.textContent = `(${message.timestamp}) ${message.author}: ${message.message}`;
        messageBox.appendChild(p);
        messageBox.scrollTop = messageBox.scrollHeight;

        // this.messageHistory.push(message);
      },
    });
  }

  sendMessage() {
    if (this.message === null || this.message.match(/^ *$/) !== null) return;

    let name = '';
    if (this.user?.username === null) return;
    name = this.user!.username;

    const hour = new Date().getHours().toString();
    const minute = new Date().getMinutes().toString();
    const second = new Date().getSeconds().toString();
    const time = hour + ':' + minute + ':' + second;

    const message = {
      message: this.message,
      timestamp: time,
      author: name,
      roomName: this.chatRoomName,
    };
    console.log("roomname: " + message.roomName);

    this.chatSocketService.sendMessage(message);
    this.messageBody.nativeElement.value = '';
    this.message = '';
  }

  onInput(evt: Event): void {
    this.message = (evt.target as HTMLInputElement).value;
  }

  minimizeChat(isMinimized:boolean){
    this.isMinimized = isMinimized;
    const iconArrowDown = this.iconArrowDown.nativeElement;
    const iconArrowUp = this.iconArrowUp.nativeElement;
    const message = this.chatBottom.nativeElement;

    if (!isMinimized) {
      iconArrowDown.style.display = 'inline';
      iconArrowUp.style.display = 'none';
      message.style.display = 'block';
    }
    else {
      iconArrowDown.style.display = 'none';
      iconArrowUp.style.display = 'inline';
      message.style.display = 'none';
    }
  }

  public openChat(channelName:string){
    let chat = <HTMLInputElement>document.getElementById("chat-popup");
    chat.style.display = "block";
    this.minimizeChat(false);
  }

  public closeChat(){
    let chat = <HTMLInputElement>document.getElementById("chat-popup");
    chat.style.display = "none";
  }

  public openChatWindow(){
    this.closeChat();
  }

  openChatPopout() {
    this.isPopoutOpen = true;
  }

  closeChatPopout() {
    this.isPopoutOpen = false;
  }
}
