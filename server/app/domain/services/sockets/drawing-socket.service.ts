import {
  COLLABORATIVE_DRAWING_NAMESPACE,
  DRAW_EVENT_NAME,
} from '../../constants/socket-constants';
import { SocketServiceInterface } from '@app/domain/interfaces/socket.interface';
import { injectable } from 'inversify';
import { Server, Socket } from 'socket.io';

@injectable()
export class DrawingSocketService extends SocketServiceInterface {
  init(io: Server) {
    super.init(io, COLLABORATIVE_DRAWING_NAMESPACE);
  }

  protected setSocketListens(socket: Socket): void {
    this.listenDrawingCommand(socket);
  }

  private listenDrawingCommand(socket: Socket): void {
    socket.on(DRAW_EVENT_NAME, (shape) => {
      console.log(shape);
    });
  }
}
