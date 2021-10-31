import { Component, ElementRef, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs';
import { AuthenticationService } from 'src/app/modules/authentication';
import { User } from '../authentication/models/user';
import { ChatSocketService } from './services/chat-socket.service';
import { ChatService } from './services/chat.service';
import { TextChannelService } from './services/text-channel.service';

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
  canals: string[] = ['hello', 'world', 'patate'];
  canalName: string;
  chatStatus:boolean;
  isChatWindowOpen = false;
  isPopoutOpen = false;

  @Input() private chatRoomName = 'default';

  constructor(
    private authenticationService: AuthenticationService,
    private chatSocketService: ChatSocketService,
    private chatService: ChatService,
    private textChannelService: TextChannelService,
  ) {
    this.authenticationService.currentUserObservable.subscribe(
      (user) => (this.user = user),
    );
    this.chatService.toggleChatOverlay.subscribe(canalName=>{
      this.canalName=canalName;
      this.openChat(canalName);
    });
  }

  ngOnInit(): void {
    this.keyListener();
    this.receiveMessage();
    this.chatSocketService.connect();
    this.chatSocketService.joinRoom(this.chatRoomName);

    // TODO: change this test to get accessed chat from channel list
    this.textChannelService.getChannel('617e4fd7e58fe02f51d7524e').subscribe((channel) => {
      this.canalName = channel.name;
    })
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

    this.chatSocketService.sendMessage(message);
    this.messageBody.nativeElement.value = '';
    this.message = '';
  }

  onInput(evt: Event): void {
    this.message = (evt.target as HTMLInputElement).value;
  }

  reduceChat(isReduce:boolean){
    const iconArrowDown = this.iconArrowDown.nativeElement;
    const iconArrowUp = this.iconArrowUp.nativeElement;
    const message = this.chatBottom.nativeElement;

    if (!isReduce) {
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

  public openChat(canalName:string){
    if (this.isChatWindowOpen)return;

    let chat = <HTMLInputElement>document.getElementById("chat-popup");
    chat.style.display = "block";
    this.reduceChat(false);
  }

  public closeChat(){
    let chat = <HTMLInputElement>document.getElementById("chat-popup");
    chat.style.display = "none";
  }

  public openChatWindow(){
    this.closeChat();
    this.isChatWindowOpen=true;
  }

  openChatPopout() {
    this.isPopoutOpen = true;
  }

  closeChatPopout() {
    this.isPopoutOpen = false;
  }
}
