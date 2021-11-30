import mongoose, { Document, Model } from 'mongoose';

export interface PrivacySettingInterface extends Document {
  email: boolean;
  firstName: boolean;
  lastName: boolean;
}

export const PrivacySettingSchema = new mongoose.Schema({
  email: { type: Boolean, default: false },
  firstName: { type: Boolean, default: false },
  lastName: { type: Boolean, default: false },
});

export const PrivacySetting: Model<PrivacySettingInterface> = mongoose.model(
  'PrivacySetting',
  PrivacySettingSchema,
);
