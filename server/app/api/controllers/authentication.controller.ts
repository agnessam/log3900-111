import { TYPES } from '../../domain/constants/types';
import { Request, Response } from 'express';
import { inject } from 'inversify';
import {
	controller,
	httpPost,
	request,
	response,
} from 'inversify-express-utils';
import {
	authLoginMiddleware,
	authRegisterMiddleware,
} from '../middleware/auth_middleware';
import { UserRepository } from '../../infrastructure/data_access/repositories/user_repository';

@controller('/auth')
export class AuthenticationController {
	@inject(TYPES.UserRepository) public userRepository: UserRepository;

	@httpPost('/register', authRegisterMiddleware)
	public register() {}

	@httpPost('/login', authLoginMiddleware)
	public login() {}

	@httpPost('/logout')
	public logout(@request() req: Request, @response() res: Response) {
		console.log(req.body.username + ' just logged out.');
		req.logout();
		res.send(true);
	}
}
