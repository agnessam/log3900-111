export interface Drawing {
  _id: string;
  dataUri: string;
  ownerId: string;
  ownerModel: string;
  name: string;

  privacyLevel: string;
  password: string;

  createdAt: string;
  updatedAt: string;
}
