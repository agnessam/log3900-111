import { injectable } from 'inversify';
import { STATUS } from '../constants/status';
import { StatusServiceInterface } from '../interfaces/status-service.interface';

@injectable()
export class StatusService implements StatusServiceInterface {
  userStatus: Map<string, STATUS>;

  updateStatus(userId: string, status: STATUS) {
    this.userStatus.set(userId, status);
  }

  setOffline(userId: string) {
    this.userStatus.delete(userId);
  }
}
