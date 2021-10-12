import { TYPES } from '../../domain/constants/types';
import cors from 'cors';
import express from 'express';
import { Container, ContainerModule } from 'inversify';
import { InversifyExpressServer } from 'inversify-express-utils';
import passport from 'passport';

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
		app.listen(appPort);

		container
			.bind<express.Application>(TYPES.Application)
			.toConstantValue(app);
		return app;
	} else {
		return container.get<express.Application>(TYPES.Application);
	}
};
