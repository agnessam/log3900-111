import { Component, OnDestroy, OnInit, Input, ElementRef, ViewChild } from "@angular/core";
import { Subscription } from "rxjs";
import { AuthenticationService } from "src/app/modules/authentication";
import { User } from "../authentication/models/user";
import { ChatSocketService } from "./services/chat-socket.service";

@Component({
  selector: "chat",
  templateUrl: "./chat.component.html",
  styleUrls: ["./chat.component.scss"],
})
export class ChatComponent implements OnInit, OnDestroy {
  public user: User | null;
  public chatSubscribiption: Subscription;
  @Input() public chatRoomName: string = "default";
  @ViewChild('messageBody', { static: false }) private messageBody: ElementRef<HTMLInputElement>;
  @ViewChild('chatBox', { static: false }) private chatBox: ElementRef<HTMLInputElement>;
  mes = "";

  constructor(
    private authenticationService: AuthenticationService,
    private chatSocketService: ChatSocketService,
  ) {
    this.authenticationService.currentUserObservable.subscribe(
      (user) => (this.user = user)
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

  // Note: this event listener listens throughout the whole app

  // @HostListener('window:keydown', ['$event'])
  public keyListener() {
    console.log('entered key listener')
    window.addEventListener("keydown", (event) => {
      console.log('entered event listener')
      if (event.key === "Enter") {
        console.log('entered enter key')
        this.sendMessage();
      }
    });
    // if (event.key === "Enter") {
    //   console.log('enter key pressed')
    //   this.sendMessage();
    // }
  }

  public receiveMessage() {
    this.chatSubscribiption = this.chatSocketService.chatSubject.subscribe({
      next: (message) => {
        // let messageBox = <HTMLInputElement>document.getElementById("chatbox");
        let messageBox = this.chatBox.nativeElement;
        const p = document.createElement("div");
        p.textContent = `(${message.timestamp}) ${message.author}: ${message.message}`;
        messageBox.appendChild(p);
        messageBox.scrollTop = messageBox.scrollHeight;
      },
    });
  }

  sendMessage() {
    // let mes = (<HTMLInputElement>document.getElementById("usermsg")).value;
    // let mes = on
    if (this.mes === null || this.mes.match(/^ *$/) !== null) return;

    let name = "";
    if (this.user?.username === null) return;
    name = this.user!.username;

    let hour = new Date().getHours().toString();
    let minute = new Date().getMinutes().toString();
    let second = new Date().getSeconds().toString();
    let time = hour + ":" + minute + ":" + second;

    let message = {
      message: this.mes,
      timestamp: time,
      author: name,
      roomName: this.chatRoomName,
    };

    this.chatSocketService.sendMessage(message);
    this.messageBody.nativeElement.value = "";
    this.mes = "";
    // (<HTMLInputElement>document.getElementById("usermsg")).value = "";
  }

  onInput(evt: Event): void {
    this.mes = (evt.target as HTMLInputElement).value;
    
  }
}
