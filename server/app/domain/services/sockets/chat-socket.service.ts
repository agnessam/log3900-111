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
import { TextChannelRepository } from '@app/infrastructure/data_access/repositories/text_channel_repository';

@injectable()
export class ChatSocketService extends SocketServiceInterface {
	@inject(TYPES.MessageRepository) public messageRepository: MessageRepository;
	@inject(TYPES.TextChannelRepository) public textChannelRepository: TextChannelRepository;

	messageHistory: Map<string, Set<MessageInterface>> = new Map();
	io: Server;
	
	init(io: Server) {
		this.io = io;
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
				this.messageHistory.set(message.roomName, new Set());
			} 
			this.messageHistory.get(message.roomName)?.add(message);
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
				this.emitHistory(roomName, Array.from((this.messageHistory.get(roomName) as Set<MessageInterface>).values()));
			}

			console.log(`number of users in ${roomName} : ${this.namespace.adapter.rooms.get(roomName)?.size}`)
		});
	}

	private emitHistory(roomName: string, history: MessageInterface[]) {
		this.namespace.to(roomName).emit(ROOM_EVENT_NAME, history);
	}

	protected listenLeaveRoom(socket: Socket) {
		socket.on(LEAVE_ROOM_EVENT_NAME, (roomName: string) => {
			console.log(`User has left room ${roomName}`);
			socket.leave(roomName);

			if (this.namespace.adapter.rooms.get(roomName)?.size === undefined && this.messageHistory.has(roomName)) {
				const currentMessages = Array.from((this.messageHistory.get(roomName) as Set<MessageInterface>).values());
				this.textChannelRepository.getChannelsByName(roomName).then((rooms) => {
					this.textChannelRepository.getMessages(rooms[0]._id).then((messages) => {
						const filtered = currentMessages.filter((message) => !messages.some((dbMessage) =>
							message.author === dbMessage.author &&
							message.message === dbMessage.message &&
							message.timestamp === dbMessage.timestamp &&
							message.roomName === dbMessage.roomName));
						this.messageRepository.storeMessages(filtered);
					})
				})
			}
			console.log(`number of users in ${roomName} : ${this.namespace.adapter.rooms.get(roomName)?.size}`)
		});
	}

	protected listenDisconnect(socket: Socket) {
		super.listenDisconnect(socket);
	}
}
