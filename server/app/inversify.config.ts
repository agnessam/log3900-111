import { Container } from 'inversify';
import { Application } from './app';
import { helloWorldController } from './controllers/hello-world.controller';
import { ChatSocketService } from './services/socket-namespaces/chat-socket.service';
import { Server } from './server';
import { TYPES } from './types';

const container: Container = new Container();

container.bind(TYPES.Server).to(Server);
container.bind(TYPES.Application).to(Application);
container.bind(TYPES.HelloWorldController).to(helloWorldController);
container.bind(TYPES.ChatSocketService).to(ChatSocketService);

export { container };
