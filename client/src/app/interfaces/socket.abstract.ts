import { io } from "socket.io-client";
import {
  ROOM_EVENT_NAME,
  LEAVE_ROOM_EVENT_NAME,
} from "src/app/services/socket/socket.constant";
import { environment } from 'src/environments/environment';

export abstract class AbstractSocketService {
  readonly SERVER_URL: string = environment.serverRawURL;
  protected namespaceSocket: any;
  protected isConnected: boolean;

  // Initialisation du socket namespace ainsi que toutes les
  // réponses socket.
  protected init(namespaceName: string) {
    this.namespaceSocket = io(`${this.SERVER_URL}/${namespaceName}`);
    this.disconnect();
    this.isConnected = false;
    this.setSocketListens();
  }

  // Défini tous les socket.on du socket en question.
  protected abstract setSocketListens(): void;

  // Configuration du room en fonction du nom du room envoyé par le client
  joinRoom(roomName: string) {
    this.emit(ROOM_EVENT_NAME, roomName);
  }

  leaveRoom(roomName: string): void {
    this.emit(LEAVE_ROOM_EVENT_NAME, roomName);
  }

  connect(): void {
    this.namespaceSocket.connect();
  }

  disconnect(): void {
    this.namespaceSocket.disconnect();
  }

  protected emit(event: string, data: any): void {
    this.namespaceSocket.emit(event, data);
  }
}
