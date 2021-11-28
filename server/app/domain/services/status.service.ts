import { injectable } from 'inversify';
import { STATUS } from '../constants/status';
import { StatusServiceInterface } from '../interfaces/status-service.interface';

@injectable()
export class StatusService implements StatusServiceInterface {
  userStatus: Map<string, STATUS> = new Map<string, STATUS>();

  updateStatus(userId: string, status: STATUS) {
    this.userStatus.set(userId, status);
  }

  setOffline(userId: string) {
    this.userStatus.delete(userId);
  }

  // We have to convert the hashmap into an object to be able to send it over in an HTTP Response.
  getUserStatus() {
    const status = {};
    this.userStatus.forEach((val: STATUS, key: string) => {
      status[key] = val;
    });
    return status;
  }
}
