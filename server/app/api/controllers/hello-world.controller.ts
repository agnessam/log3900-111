// import { NextFunction, Request, Response, Router } from 'express';
import {
	response,
	request,
	interfaces,
	controller,
	httpGet,
} from 'inversify-express-utils';
import express from 'express';

@controller('/hello/:helloid/poop')
export class HelloWorldController implements interfaces.Controller {
	// router: Router;

	// constructor() {
	// 	// this.configureRouter();
	// }

	@httpGet('/')
	public get(
		@request() req: express.Request,
		@response() res: express.Response,
	) {
		console.log(req);
		res.send('Hello World');
	}

	// private configureRouter(): void {
	// 	this.router = Router();

	// 	/**
	// 	 * @openapi
	// 	 * /api/v1/hello:
	// 	 *  get:
	// 	 *    description: Hello World!
	// 	 *    responses:
	// 	 *      200:
	// 	 *        description: Returns Hello World!
	// 	 */
	// 	this.router.get(
	// 		'/',
	// 		(req: Request, res: Response, _next: NextFunction) => {
	// 			res.json({
	// 				message: 'Hello World!',
	// 				user: req.user,
	// 				token: req.query.secret_token,
	// 			});
	// 		},
	// 	);
	// }
}
