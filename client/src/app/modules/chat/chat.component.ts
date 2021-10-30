import { Component, ElementRef, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs';
import { AuthenticationService } from 'src/app/modules/authentication';
import { User } from '../authentication/models/user';
import { ChatSocketService } from './services/chat-socket.service';
import { ChatService } from './services/chat.service';

@Component({
  selector: 'chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss'],
})
export class ChatComponent implements OnInit, OnDestroy {
  user: User | null;
  chatSubscribiption: Subscription;
  @ViewChild('messageBody', { static: false }) private messageBody: ElementRef<HTMLInputElement>;
  @ViewChild('chatBox', { static: false }) private chatBox: ElementRef<HTMLInputElement>;
  message = '';
  canals: string[] = ['hello', 'world', 'patate'];
  canal: string;
  chatStatus:boolean;
  isChatWindowOpen = false;

  @Input() private chatRoomName = 'default';

  constructor(
    private authenticationService: AuthenticationService,
    private chatSocketService: ChatSocketService,
    private chatService: ChatService,
  ) {
    this.authenticationService.currentUserObservable.subscribe(
      (user) => (this.user = user),
    );
    this.chatStatus = true;
    this.chatService.toggle.subscribe((status)=>{
      this.chatStatus = status;
      this.toggleDisplay(status);
    });

    this.canal = 'Canal name';
  }

  ngOnInit(): void {
    this.keyListener();
    this.receiveMessage();
    this.chatSocketService.connect();
    this.chatSocketService.joinRoom(this.chatRoomName);
  }

  ngOnDestroy(): void {
    this.chatSocketService.leaveRoom(this.chatRoomName);
    this.chatSocketService.disconnect();
    this.chatSubscribiption.unsubscribe();
  }

  // Note: this event listener listens throughout the whole app
  keyListener() {
    window.addEventListener('keydown', (event) => {
      if (event.key === 'Enter') {
        console.log('entered enter key');
        this.sendMessage();
      }
    });
  }

  receiveMessage() {
    this.chatSubscribiption = this.chatSocketService.chatSubject.subscribe({
      next: (message) => {
        const messageBox = this.chatBox.nativeElement;
        const p = document.createElement('div');
        p.textContent = `(${message.timestamp}) ${message.author}: ${message.message}`;
        messageBox.appendChild(p);
        messageBox.scrollTop = messageBox.scrollHeight;
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
    const iconArrowDown = document.getElementById('ArrowDown') as HTMLInputElement;
    const iconArrowUp = document.getElementById('ArrowUp') as HTMLInputElement;
    const message = document.getElementById('chat-bottom') as HTMLInputElement;

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

  toggleDisplay(showChat:boolean){
    if (this.isChatWindowOpen)return;

    const chat = document.getElementById('chat-popup') as HTMLInputElement;

    if(showChat) {
      chat.style.display = 'block';
      this.reduceChat(false);
    }
    else chat.style.display = 'none';
  }

  openChatWindow(){
    this.toggleDisplay(false);
    this.isChatWindowOpen=true;

    // TODO ajout du code pour ouvrir le chat fenetré
    // Si tu pouvais mettre la variable isChatWindowOpen a false lorsqu'on le ferme ca serait bien aussi
  }
}
