import { injectable } from "inversify";
import { Namespace, Server, Socket } from "socket.io";
import {
  ROOM_EVENT_NAME,
  CONNECTION_EVENT_NAME,
  DISCONNECTION_EVENT_NAME,
} from "@app/services/socket-constants";
import "reflect-metadata";

@injectable()
export abstract class SocketServiceInterface {
  protected namespace: Namespace;

  // Initialisation du socket namespace ainsi que toutes les
  // réponses socket.
  init(io: Server, namespaceName: string) {
    this.namespace = io.of(namespaceName);
    this.namespace.on(CONNECTION_EVENT_NAME, (socket: Socket) => {
      console.log(`${namespaceName} socket user has connected.`);
      this.setOnRoom(socket);
      this.setupSocketOns(socket);
      this.setOnDisconnect(socket);
    });
  }

  // Défini tous les socket.on du socket en question.
  protected abstract setupSocketOns(socket: Socket): void;

  // Configuration du room en fonction du nom du room envoyé par le client
  protected setOnRoom(socket: Socket) {
    socket.on(ROOM_EVENT_NAME, (roomName: string) => {
      console.log(`User has joined room ${roomName}`);
      socket.join(roomName);
    });
  }

  // Méthode par défaut sur déconnexion d'un utilisateur sur le namespace.
  protected setOnDisconnect(socket: Socket) {
    socket.on(DISCONNECTION_EVENT_NAME, () => {
      console.log(`User from namespace ${this.namespace} disconnected`);
    });
  }
}
