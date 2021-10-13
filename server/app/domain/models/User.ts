import mongoose, { Document, Model } from 'mongoose';
import bcrypt from 'bcrypt';

export interface UserInterface extends Document {
	username: string;
	description: string;

	email: string;
	password: string;
	firstName: string;
	lastName: string;

	isValidPassword(password: string): Promise<boolean>;
}

const UserSchema = new mongoose.Schema({
	username: { type: String, required: true, index: { unique: true } },
	description: String,

	email: { type: String, required: true, index: { unique: true } },
	password: { type: String, required: true },
	firstName: { type: String, required: true },
	lastName: { type: String, required: true },
});

UserSchema.pre('save', async function (next) {
	const hash = await bcrypt.hash(this.password, 10);
	this.password = hash;
	next();
});

UserSchema.methods.isValidPassword = async function (password) {
	const user = this;
	const compare = await bcrypt.compare(password, user.password);
	return compare;
};

export const User: Model<UserInterface> = mongoose.model('Users', UserSchema);