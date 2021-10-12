import mongoose from 'mongoose';

var UserSchema = new mongoose.Schema({
	username: String,
	description: String,

	email: String,
	password: String,
	firstName: String,
	lastName: String,
});

export const User = mongoose.model('User', UserSchema);
