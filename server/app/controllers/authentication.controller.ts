import { Router } from 'express';
import { injectable } from 'inversify';
import passport from 'passport';
import jwt from 'jsonwebtoken';

@injectable()
export class AuthenticationController {
	router: Router;

	constructor() {
		this.configureRouter();
	}

	private configureRouter() {
		this.router = Router();

		this.router.get('/signup', (req, res, next) => {
			res.json({ message: 'Hit signup' });
		});

		this.router.post('/login', async (req, res, next) => {
			passport.authenticate('login', async (err, user, info) => {
				try {
					if (err || !user) {
						const error = new Error('An error occurred.');
						return next(error);
					}
					const body = { _id: user._id, username: user.username };
					const token = jwt.sign({ user: body }, 'TOP_SECRET');

					return res.json({ user, token });
				} catch (error) {
					return next(error);
				}
			})(req, res, next);
		});
	}
}
