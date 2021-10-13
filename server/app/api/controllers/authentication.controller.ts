import { Request, Response } from 'express';
import {
	controller,
	httpPost,
	request,
	response,
} from 'inversify-express-utils';
import { authLoginMiddleware } from '../middleware/auth_middleware';

@controller('/auth')
export class AuthenticationController {
	@httpPost('/login', authLoginMiddleware)
	public login() {}

	@httpPost('/logout')
	public logout(@request() req: Request, @response() res: Response) {
		console.log(req.body.username + ' just logged out.');
		req.logout();
		res.send(true);
	}
}
