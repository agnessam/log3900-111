import {
  COLLABORATIVE_DRAWING_NAMESPACE,
  DRAW_EVENT,
} from '../../constants/socket-constants';
import { SocketServiceInterface } from '@app/domain/interfaces/socket.interface';
import { injectable } from 'inversify';
import { Server, Socket } from 'socket.io';
import { DrawingCommand } from '@app/domain/interfaces/drawing-command.interface';

@injectable()
export class DrawingSocketService extends SocketServiceInterface {
  init(io: Server) {
    super.init(io, COLLABORATIVE_DRAWING_NAMESPACE);
  }

  protected setSocketListens(socket: Socket): void {
    this.listenDrawingCommand(socket);
  }

  private listenDrawingCommand(socket: Socket): void {
    socket.on(DRAW_EVENT, (drawingCommand: DrawingCommand) => {
      console.log(drawingCommand);
      this.emitDrawingCommand(drawingCommand);
    });
  }

  private emitDrawingCommand(drawingCommand: DrawingCommand): void {
    this.namespace.to(drawingCommand.roomName).emit(DRAW_EVENT, drawingCommand);
  }
}

// SynchronisationService
//  <- DrawingService
//  <- ShapeFactory
