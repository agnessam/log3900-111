import { SocketServiceInterface } from '../../interfaces/socket.interface';
import { inject, injectable } from 'inversify';
import {
	CHAT_NAMESPACE_NAME,
	CONNECTION_EVENT_NAME,
	LEAVE_ROOM_EVENT_NAME,
	ROOM_EVENT_NAME,
	TEXT_MESSAGE_EVENT_NAME,
} from '../../constants/socket-constants';
import { Server, Socket } from 'socket.io';
import { TYPES } from '@app/domain/constants/types';
import { MessageRepository } from '@app/infrastructure/data_access/repositories/message_repository';
import { MessageInterface } from '@app/domain/models/Message';

@injectable()
export class ChatSocketService extends SocketServiceInterface {
	@inject(TYPES.MessageRepository) public messageRepository: MessageRepository;
	messageHistory: Map<string, MessageInterface[]> = new Map();
	connectedUsers: Map<string, number> = new Map();

	init(io: Server) {
		this.namespace = io.of(CHAT_NAMESPACE_NAME);
		this.namespace.on(CONNECTION_EVENT_NAME, (socket: Socket) => {
			console.log(`${CHAT_NAMESPACE_NAME} socket user has connected.`);
			this.setSocketListens(socket);
		});
	}

	protected setSocketListens(socket: Socket) {
		this.listenMessage(socket);
		this.listenRoom(socket);
		this.listenLeaveRoom(socket);
		this.listenDisconnect(socket);
	}

	private listenMessage(socket: Socket): void {
		socket.on(TEXT_MESSAGE_EVENT_NAME, (message: MessageInterface) => {
			// mobile sends the json object in string format
			if (typeof message === 'string') {
				message = JSON.parse(message);
			}
			console.log(
				`${message.author} at ${message.timestamp}: ${message.message}`,
			);
			this.emitMessage(message);

			if (!this.messageHistory.has(message.roomName)) {
				this.messageHistory.set(message.roomName, []);
			} 
			this.messageHistory.get(message.roomName)?.push(message);
		});
	}

	private emitMessage(message: MessageInterface): void {
		this.namespace
			.to(message.roomName)
			.emit(TEXT_MESSAGE_EVENT_NAME, message);
	}

	protected listenRoom(socket: Socket) {
		socket.on(ROOM_EVENT_NAME, (roomName: string) => {
			console.log(`User has joined room ${roomName}`);
			socket.join(roomName);

			if (this.messageHistory.has(roomName)) {
				this.emitHistory(roomName, this.messageHistory.get(roomName) as MessageInterface[]);
			}

			if (!this.connectedUsers.has(roomName)) {
				this.connectedUsers.set(roomName, 1);
			}
			const numUsers = (this.connectedUsers.get(roomName) as number) + 1;
			this.connectedUsers.set(roomName, numUsers);
			// console.log(`number of users in ${roomName} : ${this.connectedUsers.get(roomName)}`)
		});
	}

	private emitHistory(roomName: string, history: MessageInterface[]) {
		this.namespace.to(roomName).emit(ROOM_EVENT_NAME, history);
		console.log(`message history: ${history}`);
	}

	protected listenLeaveRoom(socket: Socket) {
		socket.on(LEAVE_ROOM_EVENT_NAME, (roomName: string) => {
			console.log(`User has left room ${roomName}`);
			socket.leave(roomName);

			let numUsers = (this.connectedUsers.get(roomName) as number);
			if (numUsers > 0) {
				numUsers--;
				this.connectedUsers.set(roomName, numUsers);
			} 
			
			if (numUsers == 0) {
				this.messageRepository.storeMessages(this.messageHistory.get(roomName) as MessageInterface[]);
			}
			// console.log(`number of users in ${roomName} : ${this.connectedUsers.get(roomName)}`)
		});
	}

	protected listenDisconnect(socket: Socket) {
		super.listenDisconnect(socket);
	}
}
