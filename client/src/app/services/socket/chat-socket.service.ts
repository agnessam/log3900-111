import { Injectable } from "@angular/core";
import { Subject } from "rxjs";
import { AbstractSocketService } from "src/app/interfaces/socket.abstract";
import { Message } from "src/app/services/socket/message.model";
import {
  CHAT_NAMESPACE_NAME,
  TEXT_MESSAGE_EVENT_NAME,
} from "src/app/services/socket/socket.constant";

@Injectable({
  providedIn: 'root',
})
export class ChatSocketService extends AbstractSocketService {
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
