import cors from 'cors';
import express from 'express';
import { inject, injectable } from 'inversify';
import passport from 'passport';
import localStrategy from 'passport-local';
import JWTstrategy from 'passport-jwt';
import { ExtractJwt } from 'passport-jwt';
import swaggerJSDoc from 'swagger-jsdoc';
import swaggerUi from 'swagger-ui-express';
import { helloWorldController } from './controllers/hello-world.controller';
import { TYPES } from './types';
import logger from 'morgan';
import { AuthenticationController } from './controllers/authentication.controller';
import { DatabaseController } from './controllers/database.controller';

@injectable()
export class Application {
	private readonly swaggerOptions: swaggerJSDoc.Options;
	private readonly internalError: number = 500;
	app: express.Application;

	constructor(
		@inject(TYPES.HelloWorldController)
		private helloWorldController: helloWorldController,
		@inject(TYPES.AuthenticationController)
		private authenticationController: AuthenticationController,
		@inject(TYPES.DatabaseController) 
		private databaseController: DatabaseController,
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
		this.app.use('/api/v1/', this.authenticationController.router);
		this.app.use(
			'/api/v1/hello',
			passport.authenticate('jwt', { session: false }),
			this.helloWorldController.router,
		);
		this.app.use('/api/v1/database', this.databaseController.router);
		this.errorHandling();
	}

	// Middleware configuration
	private config(): void {
		this.app.use(logger('dev'));
		this.app.use(express.json());
		this.app.use(express.urlencoded({ extended: true }));
		this.app.use(cors());
		this.app.use(passport.initialize());
		this.configurePassport();
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

	private configurePassport() {
		passport.use(
			'login',
			new localStrategy.Strategy(
				{
					usernameField: 'username',
					passwordField: 'password',
				},
				async (username, password, done) => {
					try {
						if (
							this.authenticationController.onlineUsers.has(
								username,
							)
						) {
							console.log('Username already exists');
							return done(null, null, {
								message: 'User already exists',
							});
						}

						this.authenticationController.onlineUsers.add(username);

						console.log(
							'Users online' +
								this.authenticationController.onlineUsers,
						);

						return done(null, username, {
							message: 'Logged in succesfully ',
						});
					} catch (error) {
						return done(error);
					}
				},
			),
		);

		passport.use(
			'jwt',
			new JWTstrategy.Strategy(
				{
					secretOrKey: 'TOP_SECRET',
					jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
				},
				async (token, done) => {
					try {
						return done(null, token.user);
					} catch (error) {
						done(error);
					}
				},
			),
		);
	}
}
