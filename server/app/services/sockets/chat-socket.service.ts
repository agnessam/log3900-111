import { SocketServiceInterface } from "@app/interfaces/socket.interface";
import { Message } from "@app/interfaces/message.interface";
import { injectable } from "inversify";
import {
  CHAT_NAMESPACE_NAME,
  TEXT_MESSAGE_EVENT_NAME,
} from "@app/services/socket-constants";
import { Server, Socket } from "socket.io";

@injectable()
export class ChatSocketService extends SocketServiceInterface {
  init(io: Server) {
    super.init(io, CHAT_NAMESPACE_NAME);
  }

  protected setupSocketOns(socket: Socket) {
    this.setOnMessage(socket);
  }

  private setOnMessage(socket: Socket): void {
    socket.on(TEXT_MESSAGE_EVENT_NAME, (message: Message) => {
      console.log(
        `${message.author} at ${message.timestamp}: ${message.message}`
      );
      this.emitMessage(message);
    });
  }

  private emitMessage(message: Message): void {
    this.namespace.to(message.roomName).emit(TEXT_MESSAGE_EVENT_NAME, message);
  }
}
