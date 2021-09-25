import { Container } from 'inversify';
import { Application } from './app';
import { helloWorldController } from './controllers/hello-world.controller';
import { Server } from './server';
import { TYPES } from './types';

const container: Container = new Container();

container.bind(TYPES.Server).to(Server);
container.bind(TYPES.Application).to(Application);
container.bind(TYPES.HelloWorldController).to(helloWorldController);

export { container };
