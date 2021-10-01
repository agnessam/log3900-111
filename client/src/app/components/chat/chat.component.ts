import { Component, OnDestroy, OnInit } from "@angular/core";
import { Subscription } from "rxjs";
import { AuthenticationService } from "src/app/services/authentication/authentication.service";
import { ChatSocketService } from "src/app/services/socket/chat-socket.service";

@Component({
  selector: "chat",
  templateUrl: "./chat.component.html",
  styleUrls: ["./chat.component.scss"],
})
export class ChatComponent implements OnInit, OnDestroy {
  public username: string | null;
  public chatSubscribiption: Subscription;

  constructor(
    private authenticationService: AuthenticationService,
    private chatSocketService: ChatSocketService
  ) {
    this.authenticationService.currentUserObservable.subscribe(
      (username) => (this.username = username)
    );
  }

  ngOnInit(): void {
    this.keyListener();
    this.receiveMessage();
    this.chatSocketService.connect();
    this.chatSocketService.joinRoom("default");
  }

  ngOnDestroy(): void {
    this.chatSocketService.leaveRoom("default");
    this.chatSocketService.disconnect();
    this.chatSubscribiption.unsubscribe();
  }

  public keyListener() {
    window.addEventListener("keydown", (event) => {
      if (event.key === "Enter") {
        this.sendMessage();
      }
    });
  }

  public receiveMessage() {
    this.chatSubscribiption = this.chatSocketService.chatSubject.subscribe({
      next: (message) => {
        let messageBox = <HTMLInputElement>document.getElementById("chatbox");
        const p = document.createElement("div");
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
    if (this.username === null) return;
    name = this.username;

    let hour = new Date().getHours().toString();
    let minute = new Date().getMinutes().toString();
    let second = new Date().getSeconds().toString();
    let time = hour + ":" + minute + ":" + second;

    let message = {
      message: mes,
      timestamp: time,
      author: name,
      roomName: "default",
    };

    this.chatSocketService.sendMessage(message);
    (<HTMLInputElement>document.getElementById("usermsg")).value = "";
  }
}