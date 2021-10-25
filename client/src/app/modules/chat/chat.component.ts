import { Component, OnDestroy, OnInit, Input } from "@angular/core";
import { Subscription } from "rxjs";
import { AuthenticationService } from "src/app/modules/authentication";
import { PopoutChatService } from "src/app/services/popout-chat/popout-chat.service";
import { User } from "../authentication/models/user";
import { ChatSocketService } from "./services/chat-socket.service";

@Component({
  selector: 'chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss'],
})

export class ChatComponent implements OnInit, OnDestroy {
  public user: User | null;
  public chatSubscribiption: Subscription;
  @Input() private chatRoomName: string = "default";
  isPopout: boolean;
  chatSubscription: any;

  constructor(
    private authenticationService: AuthenticationService,
    private chatSocketService: ChatSocketService,
    private popoutChatService: PopoutChatService,
  ) {
    this.authenticationService.currentUserObservable.subscribe(
      (user) => (this.user = user)
    );
    this.isPopout = false;
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
        const messageBox = document.getElementById('chatbox') as HTMLInputElement;
        const p = document.createElement('div');
        p.textContent = `(${message.timestamp}) ${message.author}: ${message.message}`;
        messageBox.appendChild(p);
        messageBox.scrollTop = messageBox.scrollHeight;
      },
    });
  }

  sendMessage() {
    let mes = (<HTMLInputElement>document.getElementById("usermsg")).value;
    if (mes === null || mes.match(/^ *$/) !== null) return;

    let name = "";
    if (this.user?.username === null) return;
    name = this.user!.username;

    const hour = new Date().getHours().toString();
    const minute = new Date().getMinutes().toString();
    const second = new Date().getSeconds().toString();
    const time = hour + ':' + minute + ':' + second;

    const uploadButton = document.getElementById('upload-button') as HTMLInputElement;
    let file;
    if (uploadButton) {
      // @ts-ignore: Object is possibly 'null'.
      file = uploadButton.files[0];
    }
    const reader = new FileReader();

    let message = {
      message: mes,
      timestamp: time,
      author: name,
      roomName: this.chatRoomName,
    };

    if (file) {
      reader.readAsDataURL(file);
    } else {
      const mes = (document.getElementById('usermsg') as HTMLInputElement).value;
      if (mes === null || mes.match(/^ *$/) !== null) return;
      const message = {
        message: mes,
        timestamp: time,
        author: name,
        roomName: 'default',
      };
      this.chatSocketService.sendMessage(message);
    }

    (document.getElementById('usermsg') as HTMLInputElement).value = '';
  }

  previewFile() {
    const preview = (document.getElementById('image-preview') as HTMLInputElement);
    const uploadButton = document.getElementById('upload-button') as HTMLInputElement;
    let file;
    if (uploadButton) {
      // @ts-ignore: Object is possibly 'null'.
      file = uploadButton.files[0];
    }
    const reader = new FileReader();

    reader.addEventListener('load', () => {
      // convert image file to base64 string
      preview.src = reader.result as string;
    }, false);

    if (file) {
      reader.readAsDataURL(file);
    }
  }

  openPopout() {
    this.popoutChatService.openPopout();
  }
}
