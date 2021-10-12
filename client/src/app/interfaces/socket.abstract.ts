import { io } from "socket.io-client";
import {
  LEAVE_ROOM_EVENT_NAME,
  ROOM_EVENT_NAME,
} from "src/app/services/socket/socket.constant";

export abstract class AbstractSocketService {
  readonly SERVER_URL: string = 'http://10.0.2.2:3000/';
    //  "http://colorimage.us-east-1.elasticbeanstalk.com";
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
