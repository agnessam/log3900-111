import { io } from "socket.io-client";
import { ROOM_EVENT_NAME } from "src/app/services/socket/socket.constant";

export abstract class AbstractSocketService {
  readonly BASE_URL: string = "http://localhost:3000";
  protected namespaceSocket: any;

  // Initialisation du socket namespace ainsi que toutes les
  // réponses socket.
  protected init(namespaceName: string) {
    this.namespaceSocket = io(`${this.BASE_URL}/${namespaceName}`);
    this.setSocketListens();
  }

  // Défini tous les socket.on du socket en question.
  protected abstract setSocketListens(): void;

  // Configuration du room en fonction du nom du room envoyé par le client
  protected joinRoom(roomName: string) {
    this.emit(ROOM_EVENT_NAME, roomName);
  }

  protected emit(event: string, data: any): void {
    this.namespaceSocket.emit(event, data);
  }
}
