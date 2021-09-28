import { injectable } from 'inversify';
import { Namespace, Server, Socket } from "socket.io";
import { ROOM_EVENT_NAME } from "@app/services/socket-constants"; 
import 'reflect-metadata';

@injectable()
export abstract class SocketServiceInterface {
    protected namespace: Namespace;

    init(io: Server, namespaceName: string) {
        this.namespace = io.of(namespaceName);
        this.setupSocketOns();
    }

    protected abstract setupSocketOns(): void;

    protected setOnRoom(socket:Socket){
       socket.on(ROOM_EVENT_NAME, (roomName: string) => {
            socket.join(roomName);
        });
    }
}
