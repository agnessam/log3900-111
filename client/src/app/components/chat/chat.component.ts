// tslint:disable: linebreak-style
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { ChatParticipant } from 'src/app/model/chat-participant.model';
import { AuthenticationService } from 'src/app/services/authentication/authentication.service';
import { ChatSocketService } from 'src/app/services/socket/chat-socket.service';
import { ChatWindow } from './chat-window/chat-window';

@Component({
  selector: 'chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss'],
})
export class ChatComponent implements OnInit, OnDestroy {
  username: string | null;
  chatSubscription: Subscription;

  window: ChatWindow;

  constructor(
    private authenticationService: AuthenticationService,
    private chatSocketService: ChatSocketService,
  ) {
    this.authenticationService.currentUserObservable.subscribe(
      (username) => (this.username = username)
    );
    this.window = new ChatWindow('Default Channel Name', {id: '666', username: this.username?.toString()} as ChatParticipant, false);
  }

  ngOnInit(): void {
    this.keyListener();
    this.receiveMessage();
    this.chatSocketService.connect();
    this.chatSocketService.joinRoom('default');
  }

  ngOnDestroy(): void {
    this.chatSocketService.leaveRoom('default');
    this.chatSocketService.disconnect();
    this.chatSubscription.unsubscribe();
  }

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
        // const messageBox = document.getElementById('chatbox') as HTMLInputElement;
        // const p = document.createElement('div');
        // p.textContent = `(${message.timestamp}) ${message.author}: ${message.message}`;
        // messageBox.appendChild(p);
        // messageBox.scrollTop = messageBox.scrollHeight;
        this.window.messages.push(message);
      },
    });
  }

  sendMessage() {
    const mes = (document.getElementById('usermsg') as HTMLInputElement).value;
    if (mes === null || mes.match(/^ *$/) !== null) return;

    let name = '';
    if (this.username === null) return;
    name = this.username;

    const hour = new Date().getHours().toString();
    const minute = new Date().getMinutes().toString();
    const second = new Date().getSeconds().toString();
    const time = hour + ':' + minute + ':' + second;

    const message = {
      message: mes,
      timestamp: time,
      author: name,
      roomName: 'default',
    };

    this.chatSocketService.sendMessage(message);
    (document.getElementById('usermsg') as HTMLInputElement).value = '';
  }

  previewFile() {
    const preview = (document.getElementById('image-preview') as HTMLInputElement);
    // @ts-ignore: Object is possibly 'null'.
    // tslint:disable-next-line: no-non-null-assertion
    const file = (document.getElementById('upload-button')! as HTMLInputElement).files[0];
    const reader = new FileReader();

    reader.addEventListener('load', () => {
      // convert image file to base64 string
      preview.src = reader.result as string;
    }, false);

    if (file) {
      reader.readAsDataURL(file);
    }
  }
}
