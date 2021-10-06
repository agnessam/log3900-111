import { Container } from 'inversify';
import { Application } from './app';
import { AuthenticationController } from './controllers/authentication.controller';
import { helloWorldController } from './controllers/hello-world.controller';
import { ChatSocketService } from './services/sockets/chat-socket.service';
import { Server } from './server';
import { TYPES } from './types';
import {DatabaseController} from './controllers/database.controller';
import { DatabaseService } from './services/database.service';


const container: Container = new Container();

container.bind(TYPES.Server).to(Server);
container.bind(TYPES.Application).to(Application);
container.bind(TYPES.HelloWorldController).to(helloWorldController);
container.bind(TYPES.AuthenticationController).to(AuthenticationController);
container.bind(TYPES.ChatSocketService).to(ChatSocketService);
container.bind(TYPES.DatabaseController).to(DatabaseController);
container.bind(TYPES.DatabaseService).to(DatabaseService);

export { container };


