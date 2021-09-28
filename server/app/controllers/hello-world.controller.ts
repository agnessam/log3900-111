import { NextFunction, Request, Response, Router } from 'express';
import { injectable } from 'inversify';

@injectable()
export class helloWorldController {
	router: Router;

	constructor() {
		this.configureRouter();
	}

	private configureRouter(): void {
		this.router = Router();

		/**
		 * @openapi
		 * /api/v1/hello:
		 *  get:
		 *    description: Hello World!
		 *    responses:
		 *      200:
		 *        description: Returns Hello World!
		 */
		this.router.get(
			'/',
			(req: Request, res: Response, _next: NextFunction) => {
				res.json({
					message: 'Hello World!',
					user: req.user,
					token: req.query.secret_token,
				});
			},
		);
	}
}
