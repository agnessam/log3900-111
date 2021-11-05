import {
  COLLABORATIVE_DRAWING_NAMESPACE,
  CONFIRM_DRAW_EVENT,
  IN_PROGRESS_DRAW_EVENT,
  CONFIRM_ERASE_EVENT,
  START_SELECTION_EVENT,
  CONFIRM_SELECTION_EVENT,
  TRANSFORM_SELECTION_EVENT,
} from '../../constants/socket-constants';
import { SocketServiceInterface } from '../../../domain/interfaces/socket.interface';
import { injectable } from 'inversify';
import { Server, Socket } from 'socket.io';
import { DrawingCommand } from '../../../domain/interfaces/drawing-command.interface';

@injectable()
export class DrawingSocketService extends SocketServiceInterface {
  init(io: Server) {
    super.init(io, COLLABORATIVE_DRAWING_NAMESPACE);
  }

  protected setSocketListens(socket: Socket): void {
    this.listenInProgressDrawingCommand(socket);
    this.listenConfirmDrawingCommand(socket);
    this.listenConfirmEraseCommand(socket);

    // Listens related to the selection functionnality.
    this.listenStartSelectionCommand(socket);
    this.listenConfirmSelectionCommand(socket);
    this.listenTransformSelectionCommand(socket);
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

  private listenConfirmEraseCommand(socket: Socket): void {
    socket.on(CONFIRM_ERASE_EVENT, (eraseCommand: any) => {
      this.emitConfirmEraseCommand(eraseCommand, socket);
    });
  }

  private listenStartSelectionCommand(socket: Socket): void {
    socket.on(START_SELECTION_EVENT, (selectionCommand: any) => {
      this.emitStartSelectionCommand(selectionCommand, socket);
    });
  }

  private listenConfirmSelectionCommand(socket: Socket): void {
    socket.on(CONFIRM_SELECTION_EVENT, (selectionCommand: any) => {
      this.emitConfirmSelectionCommand(selectionCommand, socket);
    });
  }

  private listenTransformSelectionCommand(socket: Socket): void {
    socket.on(TRANSFORM_SELECTION_EVENT, (selectionCommand: any) => {
      this.emitTransformSelectionCommand(selectionCommand, socket);
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

  private emitConfirmEraseCommand(drawingCommand: any, socket: Socket): void {
    socket
      .to(drawingCommand.roomName)
      .emit(CONFIRM_ERASE_EVENT, drawingCommand);
  }

  private emitConfirmDrawingCommand(
    drawingCommand: DrawingCommand,
    socket: Socket,
  ): void {
    socket.to(drawingCommand.roomName).emit(CONFIRM_DRAW_EVENT, drawingCommand);
  }

  // Emits related to the selection functionnality.

  private emitStartSelectionCommand(drawingCommand: any, socket: Socket): void {
    socket
      .to(drawingCommand.roomName)
      .emit(START_SELECTION_EVENT, drawingCommand);
  }

  private emitConfirmSelectionCommand(
    drawingCommand: any,
    socket: Socket,
  ): void {
    socket
      .to(drawingCommand.roomName)
      .emit(CONFIRM_SELECTION_EVENT, drawingCommand);
  }

  private emitTransformSelectionCommand(
    drawingCommand: any,
    socket: Socket,
  ): void {
    console.log(drawingCommand);
    socket
      .to(drawingCommand.roomName)
      .emit(TRANSFORM_SELECTION_EVENT, drawingCommand);
  }
}
