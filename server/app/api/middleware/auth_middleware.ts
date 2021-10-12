import { container } from '@app/infrastructure/ioc/ioc_container';
import { NextFunction, Request, Response } from 'express';
import { Container } from 'inversify';
import jwt from 'jsonwebtoken';
import passport from 'passport';
import JWTstrategy, { ExtractJwt } from 'passport-jwt';
import localStrategy from 'passport-local';

// This middleware function is performed first in the login
// middleware function calls. It verifies the credentials of user
// against the database.
export const passportLoginMiddleware = () => {
	passport.use(
		'login',
		new localStrategy.Strategy(
			{
				usernameField: 'username',
				passwordField: 'password',
			},
			async (username, password, done) => {
				try {
					// TODO: Define logic for database fetching
					return done(null, username, {
						message: 'Logged in succesfully',
					});
				} catch (error) {
					return done(error);
				}
			},
		),
	);
};

// This middleware sets up the JWT strategy that will create a JWT token
// for the user.
export const jwtStrategyMiddleware = () => {
	passport.use(
		'jwt',
		new JWTstrategy.Strategy(
			{
				secretOrKey: 'SIMPSRISE',
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
};

// Provides the middleware function that will be called when the
// /auth/login endpoint is hit.
const authLoginMiddlewareFactory = (container: Container) => {
	return async (req: Request, res: Response, next: NextFunction) => {
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
						id: user.id,
						username: user.username,
					};

					const token = jwt.sign({ user: body }, 'SIMPSRISE');

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
	};
};

const authLoginMiddleware = authLoginMiddlewareFactory(container);

export { authLoginMiddleware };
