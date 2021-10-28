import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { AuthenticationService } from 'src/app/modules/authentication';
import { User } from '../authentication/models/user';
// import { PopoutData, POPOUT_MODAL_DATA } from './models/popout.tokens';
import { ChatSocketService } from './services/chat-socket.service';
import { PopoutChatService } from './services/popout-chat.service';
// import { TextChannelService } from './services/text-channel.service';

@Component({
  selector: 'chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss'],
})
export class ChatComponent implements OnInit, OnDestroy {
  user: User | null;
  chatSubscribiption: Subscription;
  @Input() private chatRoomName = 'default';

  constructor(
    private authenticationService: AuthenticationService,
    private chatSocketService: ChatSocketService,
    private popoutChatService: PopoutChatService,
    // private textChannelService: TextChannelService,
    // @Inject(POPOUT_MODAL_DATA) public data: PopoutData,
  ) {
    this.authenticationService.currentUserObservable.subscribe(
      (user) => (this.user = user),
    );
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

  keyListener() {
    window.addEventListener('keydown', (event) => {
      if (event.key === 'Enter') {
        this.sendMessage();
      }
    });
  }

  receiveMessage() {
    this.chatSubscribiption = this.chatSocketService.chatSubject.subscribe({
      next: (message) => {
        const messageBox = document.getElementById('chatbox') as HTMLInputElement;
        const p = document.createElement('div');
        p.textContent = `(${message.timestamp}) ${message.author}: ${message.message}`;
        messageBox.appendChild(p);
        messageBox.scrollTop = messageBox.scrollHeight;
      },
    });
  }

  sendMessage() {
    const mes = (document.getElementById('usermsg') as HTMLInputElement).value;
    if (mes === null || mes.match(/^ *$/) !== null) return;

    let name = '';
    if (this.user?.username === null) return;
    name = this.user!.username;

    const hour = new Date().getHours().toString();
    const minute = new Date().getMinutes().toString();
    const second = new Date().getSeconds().toString();
    const time = hour + ':' + minute + ':' + second;

    const message = {
      message: mes,
      timestamp: time,
      author: name,
      roomName: this.chatRoomName,
    };

    this.chatSocketService.sendMessage(message);
    (document.getElementById('usermsg') as HTMLInputElement).value = '';
  }

  openPopout() {
    const modalData = {
      // id: string,
      // owner: this.textChannelService.getChannel()
      // chatRoomName: this.chatRoomName,
    }
    this.popoutChatService.openPopout(modalData);
  }
}
