import { Component, ElementRef, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs';
import { AuthenticationService } from 'src/app/modules/authentication';
import { User } from '../authentication/models/user';
import { Message } from './models/message.model';
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
  // @ViewChild('chatBox', { static: false }) private chatBox: ElementRef<HTMLInputElement>;
  @ViewChild('arrowDown', { static: false }) private iconArrowDown: ElementRef<HTMLInputElement>;
  @ViewChild('arrowUp', { static: false }) private iconArrowUp: ElementRef<HTMLInputElement>;
  @ViewChild('chatBottom', { static: false }) private chatBottom: ElementRef<HTMLInputElement>;
  message = '';
  // saves connected rooms and message history from connection
  currentMessageHistory: Map<string, Message[]> = new Map();
  
  // connectedRooms: string[] = [];
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
    this.chatService.toggleChatOverlay.subscribe((channel) =>{
      this.chatRoomName = channel.name;
      if (!this.isPopoutOpen) this.openChat();

      if (!this.currentMessageHistory.has(channel.name)) {
        this.currentMessageHistory.set(channel.name, []);
        this.chatSocketService.joinRoom(channel.name);
      }
    });
  }

  ngOnInit(): void {
    this.keyListener();
    this.receiveMessage();
    this.chatSocketService.connect();
  }

  ngOnDestroy(): void {
    this.currentMessageHistory.forEach((_messages, roomName) => {
      this.chatSocketService.leaveRoom(roomName);
    });
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
    // appends every message from every roomname
    this.chatSubscription = this.chatSocketService.chatSubject.subscribe({
      next: (message) => {
        this.currentMessageHistory.get(message.roomName)?.push(message);

        // const messageBox = this.chatBox.nativeElement;
        // const p = document.createElement('div');
        // p.textContent = `(${message.timestamp}) ${message.author}: ${message.message}`;
        // messageBox.appendChild(p);
        // messageBox.scrollTop = messageBox.scrollHeight;
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

  openChat(){
    const chat = document.getElementById('chat-popup') as HTMLInputElement;
    chat.style.display = 'block';
    this.minimizeChat(false);
  }

  closeChat(){
    const chat = document.getElementById('chat-popup') as HTMLInputElement;
    chat.style.display = 'none';
  }

  openChatPopout() {
    this.isPopoutOpen = true;
  }

  closeChatPopout() {
    // on close using browser doesn't toggle ispopoutopen
    this.isPopoutOpen = false;
  }
}
