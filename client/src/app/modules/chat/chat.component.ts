import {
  ChangeDetectorRef,
  Component,
  ElementRef,
  OnDestroy,
  OnInit,
  ViewChild,
} from "@angular/core";
import { Subscription } from "rxjs";
import { AuthenticationService } from "src/app/modules/authentication";
import { User } from "../authentication/models/user";
import { Message } from "./models/message.model";
import { TextChannel } from "./models/text-channel.model";
import { ChatSocketService } from "./services/chat-socket.service";
import { ChatService } from "./services/chat.service";
import { TextChannelService } from "./services/text-channel.service";

@Component({
  selector: "chat",
  templateUrl: "./chat.component.html",
  styleUrls: ["./chat.component.scss"],
})
export class ChatComponent implements OnInit, OnDestroy {
  user: User | null;
  chatSubscription: Subscription;
  @ViewChild("chatBox", { static: false })
  private chatBox: ElementRef<HTMLInputElement>;

  message = "";
  // saves connected rooms and message history from connection
  connectedMessageHistory: Map<string, Set<Message>> = new Map();

  chatStatus: boolean;
  isMinimized = false;
  isPopoutOpen = false;
  hasDbMessages = false;
  loadedDbMessages = false;

  openChannel: TextChannel;

  constructor(
    private authenticationService: AuthenticationService,
    private chatSocketService: ChatSocketService,
    private chatService: ChatService,
    private textChannelService: TextChannelService,
    private ref: ChangeDetectorRef
  ) {
    this.loadedDbMessages = false;
    this.hasDbMessages = false;
    this.openChannel = {
      _id: "default",
      name: "General",
      ownerId: "default"
    }
  }

  ngOnInit(): void {
    this.authenticationService.currentUserObservable.subscribe((user) => {
      if (user) {
        this.user = user;
      } else {
        this.closeChat();
        this.closeChatPopout();
      }
    });
    this.openChatRoom();
    this.receiveMessage();
    this.leaveRoom();
    this.chatSocketService.connect();
  }

  ngOnDestroy(): void {
    this.connectedMessageHistory.forEach((_messages, roomName) => {
      this.chatSocketService.leaveRoom({
        userId: this.user!._id,
        roomName: roomName,
      });
    });
    this.chatSocketService.disconnect();
    this.chatSubscription.unsubscribe();
    this.closeChat();
    this.closeChatPopout();
  }

  openChatRoom() {
    // might want to run these in parallel (fork?)
    this.chatService.toggleChatOverlay.subscribe((channel) => {
      console.log("opening chat room: " + channel.name)
      this.openChannel = channel
      if (!this.isPopoutOpen) {
        const chat = document.getElementById("chat-popup") as HTMLInputElement;
        chat.style.display = "block";
        this.minimizeChat(false);
      }
      this.scrollDown();
      this.joinRoom(channel.name);
    });
  }

  joinRoom(channelName: string) {
    if (!this.connectedMessageHistory.has(channelName)) {
      this.connectedMessageHistory.set(channelName, new Set());
      this.chatSocketService.joinRoom({
        userId: this.user!._id,
        roomName: channelName,
      });
      this.loadedDbMessages = false;
      this.chatSocketService.messageHistory.subscribe({
        next: (history) => {
          this.connectedMessageHistory.set(
            history[0].roomName,
            new Set(history)
          );
        },
      });
    }
  }

  leaveRoom() {
    this.chatService.leaveRoomEventEmitter.subscribe((channel) => {
      this.connectedMessageHistory.delete(channel.name);
      this.chatSocketService.leaveRoom({
        userId: this.user!._id,
        roomName: channel.name,
      });
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

    if (this.user?.username === null) return;
    const name = (this.user as User).username;

    const hour = new Date().getHours().toString();
    const minute = new Date().getMinutes().toString();
    const second = new Date().getSeconds().toString();
    const time = hour + ":" + minute + ":" + second;

    this.textChannelService
      .getChannel(this.openChannel._id)
      .subscribe((channel) => {
        const message = {
          message: this.message,
          timestamp: time,
          author: name,
          roomId: channel._id,
          roomName: this.openChannel.name,
        };
        this.chatSocketService.sendMessage(message);
        this.message = "";
      });
  }

  minimizeChat(isMinimized: boolean) {
    this.isMinimized = isMinimized;
  }

  closeChat() {
    const chat = document.getElementById("chat-popup") as HTMLInputElement;
    chat.style.display = "none";
  }

  openChatPopout() {
    this.isPopoutOpen = true;
  }

  closeChatPopout(_isOut?: boolean) {
    this.isPopoutOpen = false;
    this.ref.detectChanges();
  }

  getMessageClass(author: string): string {
    if (this.user?.username === author) return "user";
    else return "other";
  }

  getMessages(): Message[] {
    if (!this.connectedMessageHistory.has(this.openChannel.name)) {
      return [];
    }
    const messages = Array.from(
      (
        this.connectedMessageHistory.get(this.openChannel.name) as Set<Message>
      ).values()
    );
    return messages;
  }

  getDatabaseMessages(channel: TextChannel): void {
    this.textChannelService
      .getChannel(channel._id)
      .subscribe((channel) => {
        this.textChannelService
          .getMessages(channel._id)
          .subscribe((messages) => {
            if (this.connectedMessageHistory.has(channel.name)) {
              this.loadedDbMessages = true;
              this.hasDbMessages = messages.length !== 0;
              if (!this.hasDbMessages) {
                return;
              }
              const currentMessages = Array.from(
                (
                  this.connectedMessageHistory.get(channel.name) as Set<Message>
                ).values()
              );
              // Filter out messages that match attributes from database (should be id but not generated yet for current)
              const filtered = currentMessages.filter(
                (message) =>
                  !messages.some(
                    (dbMessage) =>
                      message.author === dbMessage.author &&
                      message.message === dbMessage.message &&
                      message.timestamp === dbMessage.timestamp &&
                      message.roomName === dbMessage.roomName
                  )
              );
              this.connectedMessageHistory.set(
                channel.name,
                new Set(messages.concat(filtered))
              );
            }
          });
      });
  }
}
