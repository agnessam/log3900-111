import mongoose, { Document, Model, Schema } from 'mongoose';

export interface TeamInterface extends Document {
    teamName: string,
    teamMembers: string[],

    drawings: string[], 
}

const TeamSchema = new mongoose.Schema({
    teamName: { type: String, required: true, index: { unique: true } },
    teamMembers:  [String], 

    drawings: [{ type: Schema.Types.ObjectId, ref: 'Drawing' }]
});

export const Team: Model<TeamInterface> = mongoose.model('Team', TeamSchema);
