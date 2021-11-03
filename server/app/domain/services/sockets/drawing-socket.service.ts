import {
  COLLABORATIVE_DRAWING_NAMESPACE,
  CONFIRM_DRAW_EVENT,
  IN_PROGRESS_DRAW_EVENT,
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
    this.listenInProgressDrawingCommand(socket);
    this.listenConfirmDrawingCommand(socket);
  }

  private listenInProgressDrawingCommand(socket: Socket): void {
    socket.on(IN_PROGRESS_DRAW_EVENT, (drawingCommand: DrawingCommand) => {
      console.log(drawingCommand);
      this.emitInProgressDrawingCommand(drawingCommand, socket);
    });
  }

  // Will be used to store all the drawn shapes since the join of the drawing.
  // Allows users to load what was there.
  private listenConfirmDrawingCommand(socket: Socket): void {
    socket.on(CONFIRM_DRAW_EVENT, (drawingCommand: DrawingCommand) => {
      console.log(drawingCommand);
      this.emitConfirmDrawingCommand(drawingCommand, socket);
    });
  }

  private emitInProgressDrawingCommand(
    drawingCommand: DrawingCommand,
    socket: Socket,
  ): void {
    socket
      .to(drawingCommand.roomName)
      .emit(IN_PROGRESS_DRAW_EVENT, drawingCommand);
  }

  private emitConfirmDrawingCommand(
    drawingCommand: DrawingCommand,
    socket: Socket,
  ): void {
    socket.to(drawingCommand.roomName).emit(CONFIRM_DRAW_EVENT, drawingCommand);
  }
}
