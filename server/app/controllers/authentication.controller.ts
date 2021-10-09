import { Router } from 'express';
import { injectable } from 'inversify';
import passport from 'passport';
import jwt from 'jsonwebtoken';
//import DatabaseController from './database.controller';

@injectable()
export class AuthenticationController {
	onlineUsers: Set<string>;
	router: Router;

	constructor(
		//private DatabaseController: DatabaseController
		) {
		this.onlineUsers = new Set();
		this.configureRouter();
	}

	private configureRouter() {
		this.router = Router();

		//this.DatabaseController.userIsLog[0] = username = pseudo
		///this.DatabaseController.userIsLog[1] = passsword

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
						console.log(user + ' just logged in!');
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
			console.log(req.body.username + ' just logged out.');
			console.log('Online users: ');
			this.onlineUsers.forEach((user) => {
				console.log(user);
			});
			return res.send(true);
		});
	}
}
