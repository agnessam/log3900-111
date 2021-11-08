import { ChangeDetectorRef, Component, ElementRef, OnDestroy, OnInit, ViewChild,  } from '@angular/core';
import { Subscription } from 'rxjs';
import { AuthenticationService } from 'src/app/modules/authentication';
import { User } from '../authentication/models/user';
import { Message } from './models/message.model';
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
  // saves connected rooms and message history from connection
  connectedMessageHistory: Map<string, Set<Message>> = new Map();

  chatStatus: boolean;
  isMinimized = false;
  isPopoutOpen = false;
  hasDbMessages = false;
  loadedDbMessages = false;

  chatRoomName = 'General';

  constructor(
    private authenticationService: AuthenticationService,
    private chatSocketService: ChatSocketService,
    private chatService: ChatService,
    private textChannelService: TextChannelService,
    private ref: ChangeDetectorRef,
  ) {
    this.loadedDbMessages = false;
    this.hasDbMessages = false;
  }

  ngOnInit(): void {
    this.authenticationService.currentUserObservable.subscribe((user) => {
        if (user) {
          this.user = user;
        } else {
          this.closeChat();
          this.closeChatPopout();
        }
      }
    );
    this.openChatRoom();
    this.keyListener();
    this.receiveMessage();
    this.leaveRoom();
    this.chatSocketService.connect();
  }

  ngOnDestroy(): void {
    this.connectedMessageHistory.forEach((_messages, roomName) => {
      this.chatSocketService.leaveRoom(roomName);
    });
    this.chatSocketService.disconnect();
    this.chatSubscription.unsubscribe();
    this.closeChat();
    this.closeChatPopout();
  }

  // Note: this event listener listens throughout the whole app
  keyListener() {
    window.addEventListener('keydown', (event) => {
      if (event.key === 'Enter') {
        this.sendMessage();
      }
    });
  }

  openChatRoom() {
    // might want to run these in parallel (fork?)
    this.chatService.toggleChatOverlay.subscribe((channel) =>{
      this.chatRoomName = channel.name;
      if (!this.isPopoutOpen) {
        const chat = document.getElementById('chat-popup') as HTMLInputElement;
        chat.style.display = 'block';
        this.minimizeChat(false);
      }
      this.scrollDown();
      this.joinRoom(this.chatRoomName);
    });

  }

  joinRoom(channelName: string) {
    if (!this.connectedMessageHistory.has(channelName)) {
      this.connectedMessageHistory.set(channelName, new Set());
      this.chatSocketService.joinRoom(channelName);
      this.loadedDbMessages = false;
      this.chatSocketService.messageHistory.subscribe({
        next: (history) => {
          this.connectedMessageHistory.set(history[0].roomName, new Set(history));
        },
      });
    }
  }

  leaveRoom() {
    this.chatService.leaveRoomEventEmitter.subscribe((channel) => {
      this.connectedMessageHistory.delete(channel.name);
      this.chatSocketService.leaveRoom(channel.name);
      this.closeChat();
      this.closeChatPopout();
    });
  }

  receiveMessage() {
    // appends every message from every roomname
    this.chatSubscription = this.chatSocketService.chatSubject.subscribe({
      next: (message) => {
        this.joinRoom(message.roomName);
        if (!this.connectedMessageHistory.get(message.roomName)?.has(message)) {
          this.connectedMessageHistory.get(message.roomName)?.add(message);
        }
        this.scrollDown();
      },
    });
  }

  scrollDown() {
    const messageBox = this.chatBox.nativeElement;
    messageBox.scrollTop = messageBox.scrollHeight;
  }

  sendMessage() {
    if (this.message === null || this.message.match(/^ *$/) !== null) return;

    let name = '';
    if (this.user?.username === null) return;
    name = (this.user as User).username;

    const hour = new Date().getHours().toString();
    const minute = new Date().getMinutes().toString();
    const second = new Date().getSeconds().toString();
    const time = hour + ':' + minute + ':' + second;

    this.textChannelService.getChannelsByName(this.chatRoomName).subscribe((channels) => {
      const message = {
        message: this.message,
        timestamp: time,
        author: name,
        roomId: channels[0]._id,
        roomName: this.chatRoomName,
      };
      this.chatSocketService.sendMessage(message);
      this.messageBody.nativeElement.value = '';
      this.message = '';
    });
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

  closeChat(){
    const chat = document.getElementById('chat-popup') as HTMLInputElement;
    chat.style.display = 'none';
  }

  openChatPopout() {
    this.isPopoutOpen = true;
  }

  closeChatPopout(_isOut?: boolean) {
    this.isPopoutOpen = false;
    this.ref.detectChanges();
  }

  getMessageClass(author:string): string{
    if(this.user?.username === author)
      return 'user';
    else
      return 'other';
  }

  getMessages(): Message[]{
    if (!this.connectedMessageHistory.has(this.chatRoomName)) {
      return [];
    }
    const messages = Array.from((this.connectedMessageHistory.get(this.chatRoomName) as Set<Message>).values());
    console.log(messages)
    return messages;
  }

  getDatabaseMessages(channelName: string): void {
    this.textChannelService.getChannelsByName(channelName).subscribe((channels) => {
      this.textChannelService.getMessages(channels[0]._id).subscribe((messages) => {
        if (this.connectedMessageHistory.has(channelName)) {
          this.loadedDbMessages = true;
          this.hasDbMessages = messages.length !== 0;
          if (!this.hasDbMessages) {
            return;
          }
          const currentMessages = Array.from((this.connectedMessageHistory.get(channelName) as Set<Message>).values());
          // Filter out messages that match attributes from database (should be id but not generated yet for current)
          const filtered = currentMessages.filter((message) => !messages.some((dbMessage) =>
            message.author === dbMessage.author &&
            message.message === dbMessage.message &&
            message.timestamp === dbMessage.timestamp &&
            message.roomName === dbMessage.roomName));
          this.connectedMessageHistory.set(channelName, new Set(messages.concat(filtered)));
        }
      });
    });
  }
}
