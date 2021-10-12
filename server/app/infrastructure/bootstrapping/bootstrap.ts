import { TYPES } from '../../domain/constants/types';
import cors from 'cors';
import express from 'express';
import { Container, ContainerModule } from 'inversify';
import { InversifyExpressServer } from 'inversify-express-utils';
import passport from 'passport';
import { Server as SocketServer } from 'socket.io';
import { ChatSocketService } from '../../domain/services/sockets/chat-socket.service';
import '../../api/controllers/hello-world.controller';

export const boostrap = async (
	container: Container,
	appPort: number,
	dbHost: string,
	dbName: string,
	...modules: ContainerModule[]
) => {
	// TODO: Add database client here connection here.

	if (container.isBound(TYPES.Application) === false) {
		container.load(...modules);
		const server = new InversifyExpressServer(container, null, {
			rootPath: '/api',
		});

		server.setConfig((app) => {
			app.set('etag', false);
			app.use(express.json());
			app.use(express.urlencoded({ extended: false }));
			app.use(cors());
			app.use(passport.initialize());
		});

		const app = server.build();

		console.log(`Application listening on port ${appPort}`);
		let serverInstance = app.listen(appPort);

		let socketServer = new SocketServer(serverInstance, {
			cors: {
				origin: '*',
				methods: ['GET', 'POST'],
			},
		});
		const chatSocketService: ChatSocketService = container.get(TYPES.ChatSocketService);
		chatSocketService.init(socketServer);

		container
			.bind<express.Application>(TYPES.Application)
			.toConstantValue(app);
		return app;
	} else {
		return container.get<express.Application>(TYPES.Application);
	}
};
