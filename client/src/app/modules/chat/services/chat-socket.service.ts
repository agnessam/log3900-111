import { Injectable } from "@angular/core";
import { AbstractSocketService } from "src/app/shared";
import {
  CHAT_NAMESPACE_NAME,
  TEXT_MESSAGE_EVENT_NAME,
} from "../constants/socket.constant";
import { Message } from "../models/message.model";
import { Subject } from "rxjs";

@Injectable({
  providedIn: 'root',
})
export class ChatSocketService extends AbstractSocketService {
  connect() {
    throw new Error("Method not implemented.");
  }
  joinRoom(chatRoomName: string) {
    throw new Error("Method not implemented.");
  }
  leaveRoom(chatRoomName: string) {
    throw new Error("Method not implemented.");
  }
  disconnect() {
    throw new Error("Method not implemented.");
  }
  chatSubject: Subject<Message>;

  constructor() {
    super();
    this.init();
  }

  protected init(): void {
    super.init(CHAT_NAMESPACE_NAME);
    this.chatSubject = new Subject<Message>();
  }

  sendMessage(message: Message): void {
    this.emit(TEXT_MESSAGE_EVENT_NAME, message);
  }

  protected setSocketListens(): void {
    this.listenMessage();
  }

  private listenMessage(): void {
    this.namespaceSocket.on(TEXT_MESSAGE_EVENT_NAME, (message: Message) => {
      this.chatSubject.next(message);
    });
  }
}
