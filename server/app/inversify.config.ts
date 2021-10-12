import { ContainerModule } from 'inversify';
import { TYPES } from './domain/constants/types';
import { SocketServiceInterface } from './domain/interfaces/socket.interface';
import { ChatSocketService } from './domain/services/sockets/chat-socket.service';

export const referenceDataIoCModule = new ContainerModule((bind) => {
	bind<SocketServiceInterface>(TYPES.ChatSocketService)
		.to(ChatSocketService)
		.inSingletonScope();
});
