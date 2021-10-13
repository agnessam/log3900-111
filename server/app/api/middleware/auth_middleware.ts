import { NextFunction, Request, Response } from 'express';
import jwt from 'jsonwebtoken';
import passport from 'passport';
import JWTstrategy, { ExtractJwt } from 'passport-jwt';
import localStrategy from 'passport-local';
import { User } from '../../domain/models/user';

export const passportRegisterMiddleware = () => {
	passport.use(
		'register',
		new localStrategy.Strategy(
			{
				usernameField: 'username',
				passwordField: 'password',
				passReqToCallback: true,
			},
			async (req, username, password, done) => {
				try {
					const user = await User.create(req.body);
					return done(null, user);
				} catch (error) {
					done(error);
				}
			},
		),
	);
};

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
					const user = await User.findOne({ username });
					if (!user) {
						return done(null, false, {
							message: 'Username not found.',
						});
					}

					const validate = await user.isValidPassword(password);

					if (!validate) {
						return done(null, false, {
							message: 'Wrong password.',
						});
					}

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
// /auth/register endpoint is hit.
const authRegisterMiddlewareFactory = () => {
	return (req: Request, res: Response, next: NextFunction) => {
		passport.authenticate('register', (err, user, info) => {
			try {
				// TODO: Better error handling.
				if (err || !user) {
					return res.json({
						user: null,
						token: null,
						error: err,
					});
				}
				req.login(user, { session: false }, (error) => {
					const body = {
						id: user.id,
						username: user.username,
					};

					const token = jwt.sign({ user: body }, 'SIMPSRISE');

					return res.json({
						user: user,
						token: token,
						error: info,
					});
				});
			} catch (err) {
				return next(err);
			}
		})(req, res, next);
	};
};

// Provides the middleware function that will be called when the
// /auth/login endpoint is hit.
const authLoginMiddlewareFactory = () => {
	return async (req: Request, res: Response, next: NextFunction) => {
		passport.authenticate('login', async (err, user, info) => {
			try {
				if (err || !user) {
					return res.json({
						user: null,
						token: null,
						error: info,
					});
				}
				req.login(user, { session: false }, async (error) => {
					const body = {
						id: user.id,
						user: user.username,
					};

					const token = jwt.sign({ user: body }, 'SIMPSRISE');

					return res.json({
						user: user.username,
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

const authRegisterMiddleware = authRegisterMiddlewareFactory();
const authLoginMiddleware = authLoginMiddlewareFactory();

export { authRegisterMiddleware, authLoginMiddleware };
