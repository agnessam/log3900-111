export class User {
  _id: string;
  username: string;
  password: string;
  firstName: string;
  lastName: string;
  email: string;
  description: string;

  public constructor(init?: Partial<User>) {
    Object.assign(this, init);
  }
}
