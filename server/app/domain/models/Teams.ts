import mongoose, { Document, Model } from 'mongoose';

export interface TeamInterface extends Document {
    teamName: string,
    teamMembers: string[],
}

const TeamSchema = new mongoose.Schema({
    teamName: { type: String, required: true, index: { unique: true } },
    teamMembers:  [String] , 
});

export const Team: Model<TeamInterface> = mongoose.model('Teams', TeamSchema);
