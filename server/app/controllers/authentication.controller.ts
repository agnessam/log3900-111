import { Router } from 'express';
import { injectable } from 'inversify';
import passport from 'passport';
import jwt from 'jsonwebtoken';

@injectable()
export class AuthenticationController {
	onlineUsers: Set<string>;
	router: Router;

	constructor() {
		this.onlineUsers = new Set();
		this.configureRouter();
	}

	private configureRouter() {
		this.router = Router();

		this.router.post('/login', async (req, res, next) => {
			passport.authenticate('login', async (err, user, info) => {
				try {
					if (err || !user) {
						return res.json({
							username: null,
							token: null,
							error: info,
						});
					}

					req.login(user, async (error) => {
						const body = {
							_id: user._id,
							username: user.username,
						};
						const token = jwt.sign({ user: body }, 'TOP_SECRET');

						return res.json({
							username: user,
							token: token,
							error: err,
						});
					});
				} catch (error) {
					return next(error);
				}
			})(req, res, next);
		});

		this.router.post('/logout', async (req, res, next) => {
			this.onlineUsers.delete(req.body.username);
			req.logout();
			console.log(this.onlineUsers);
			return res.send(true);
		});
	}
}
