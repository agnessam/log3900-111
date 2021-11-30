import mongoose, { Document, Model } from 'mongoose';

export interface PrivacySettingInterface extends Document {
  email: boolean;
  firstName: boolean;
  lastName: boolean;
}

export const PrivacySettingSchema = new mongoose.Schema({
  searchableByEmail: { type: Boolean },
  searchableByFirstName: { type: Boolean },
  searchableByLastName: { type: Boolean },
});

export const PrivacySetting: Model<PrivacySettingInterface> = mongoose.model(
  'PrivacySetting',
  PrivacySettingSchema,
);
