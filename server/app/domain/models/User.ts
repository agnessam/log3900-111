import mongoose, { Document, Model } from 'mongoose';

export interface UserInterface extends Document {
	username: string;
	description: string;

	email: string;
	password: string;
	firstName: string;
	lastName: string;
}

const UserSchema = new mongoose.Schema({
	username: String,
	description: String,

	email: String,
	password: String,
	firstName: String,
	lastName: String,
});

export const User: Model<UserInterface> = mongoose.model('Users', UserSchema);
