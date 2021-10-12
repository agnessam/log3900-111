import 'reflect-metadata';
import { container } from './infrastructure/ioc/ioc_container';
import { boostrap } from './infrastructure/bootstrapping/bootstrap';
import { referenceDataIoCModule } from './inversify.config';

// const server: Server = container.get<Server>(TYPES.Server);

// server.init();

const runApp = async () => {
	const app = await boostrap(
		container,
		3000,
		'localhost',
		'demo',
		referenceDataIoCModule,
	);
	return app;
};

(async () => {
	await runApp();
})();
