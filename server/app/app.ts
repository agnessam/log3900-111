import cors from 'cors';
import express from 'express';
import swaggerJSDoc from 'swagger-jsdoc';
import swaggerUi from 'swagger-ui-express';
import { inject, injectable } from 'inversify';
import { helloWorldController } from './controllers/hello-world.controller';
import { TYPES } from './types';

@injectable()
export class Application {
	private readonly swaggerOptions: swaggerJSDoc.Options;
	private readonly internalError: number = 500;
	app: express.Application;

	constructor(
		@inject(TYPES.HelloWorldController)
		private helloWorldController: helloWorldController,
	) {
		this.app = express();
		this.swaggerOptions = {
			definition: {
				openapi: '3.0.0',
				info: {
					title: 'ColorImage',
					version: '1.0.0',
				},
			},
			apis: ['**/*.ts'],
		};

		this.config();
		this.bindRoutes();
	}

	bindRoutes(): void {
		this.app.use(
			'/api/v1/docs',
			swaggerUi.serve,
			swaggerUi.setup(swaggerJSDoc(this.swaggerOptions)),
		);
		this.app.use('/api/v1/hello', this.helloWorldController.router);
		this.errorHandling();
	}

	// Middleware configuration
	private config(): void {
		this.app.use(cors());
	}

	private errorHandling(): void {
		// When previous handlers have not served a request: path wasn't found
		this.app.use(
			(
				req: express.Request,
				res: express.Response,
				next: express.NextFunction,
			) => {
				const err: Error = new Error('Not Found');
				next(err);
			},
		);

		// development error handler
		// will print stacktrace
		if (this.app.get('env') === 'development') {
			// tslint:disable-next-line:no-any
			this.app.use(
				(
					err: any,
					req: express.Request,
					res: express.Response,
					next: express.NextFunction,
				) => {
					res.status(err.status || this.internalError);
					res.send({
						message: err.message,
						error: err,
					});
				},
			);
		}

		// production error handler
		// no stacktraces leaked to user (in production env only)
		// tslint:disable-next-line:no-any
		this.app.use(
			(
				err: any,
				req: express.Request,
				res: express.Response,
				next: express.NextFunction,
			) => {
				res.status(err.status || this.internalError);
				res.send({
					message: err.message,
					error: {},
				});
			},
		);
	}
}
