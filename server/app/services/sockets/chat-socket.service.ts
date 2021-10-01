import { SocketServiceInterface } from "../../interfaces/socket.interface";
import { Message } from "../../interfaces/message.interface";
import { injectable } from "inversify";
import { CHAT_NAMESPACE_NAME, TEXT_MESSAGE_EVENT_NAME } from "../../services/socket-constants";
import { Server, Socket } from "socket.io";

@injectable()
export class ChatSocketService extends SocketServiceInterface {
  init(io: Server) {
    super.init(io, CHAT_NAMESPACE_NAME);
  }

  protected setSocketListens(socket: Socket) {
    this.listenMessage(socket);
  }

  private listenMessage(socket: Socket): void {
    socket.on(TEXT_MESSAGE_EVENT_NAME, (message: Message) => {
      // mobile sends the json object in string format
      if (typeof message === "string") {
        message = JSON.parse(message);
      }

      console.log(`${message.author} at ${message.timestamp}: ${message.message}`);
      this.emitMessage(message);
    });
  }

  private emitMessage(message: Message): void {
    this.namespace.to(message.roomName).emit(TEXT_MESSAGE_EVENT_NAME, message);
  }
}
