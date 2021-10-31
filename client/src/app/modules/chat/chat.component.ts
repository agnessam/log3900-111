import { Component, OnDestroy, OnInit, Input } from "@angular/core";
import { Subscription } from "rxjs";
import { AuthenticationService } from "src/app/modules/authentication";
import { User } from "../authentication/models/user";
import { ChatSocketService } from "./services/chat-socket.service";
import { ChatService } from "./services/chat.service";

@Component({
  selector: "chat",
  templateUrl: "./chat.component.html",
  styleUrls: ["./chat.component.scss"],
})
export class ChatComponent implements OnInit, OnDestroy {
  public user: User | null;
  public chatSubscribiption: Subscription;
  public canalName: string;
  public isChatWindowOpen = false;

  @Input() private chatRoomName: string = "default";

  constructor(
    private authenticationService: AuthenticationService,
    private chatSocketService: ChatSocketService,
    private chatService: ChatService,
  ) {
    this.authenticationService.currentUserObservable.subscribe(
      (user) => (this.user = user)
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
  }

  ngOnDestroy(): void {
    this.chatSocketService.leaveRoom(this.chatRoomName);
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
    if (this.user?.username === null) return;
    name = this.user!.username;

    let hour = new Date().getHours().toString();
    let minute = new Date().getMinutes().toString();
    let second = new Date().getSeconds().toString();
    let time = hour + ":" + minute + ":" + second;

    let message = {
      message: mes,
      timestamp: time,
      author: name,
      roomName: this.chatRoomName,
    };

    this.chatSocketService.sendMessage(message);
    (<HTMLInputElement>document.getElementById("usermsg")).value = "";
  }

  public reduceChat(isReduce:boolean){
    let iconArrowDown = <HTMLInputElement>document.getElementById("ArrowDown");
    let iconArrowUp = <HTMLInputElement>document.getElementById("ArrowUp");
    let message = <HTMLInputElement>document.getElementById("chat-bottom");

    if (!isReduce) {
      iconArrowDown.style.display = "inline";
      iconArrowUp.style.display = "none";
      message.style.display = "block";
    }
    else {
      iconArrowDown.style.display = "none";
      iconArrowUp.style.display = "inline";
      message.style.display = "none";
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

    // TODO ajout du code pour ouvrir le chat fenetré
    // Si tu pouvais mettre la variable isChatWindowOpen a false lorsqu'on le ferme ca serait bien aussi
  }
}
