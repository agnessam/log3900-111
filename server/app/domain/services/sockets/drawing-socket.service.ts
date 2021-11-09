import {
  COLLABORATIVE_DRAWING_NAMESPACE,
  CONFIRM_DRAW_EVENT,
  IN_PROGRESS_DRAW_EVENT,
  CONFIRM_ERASE_EVENT,
  START_SELECTION_EVENT,
  CONFIRM_SELECTION_EVENT,
  TRANSFORM_SELECTION_EVENT,
  DELETE_SELECTION_EVENT,
  UPDATE_DRAWING_EVENT,
  FETCH_DRIVING_EVENT,
  UPDATE_DRAWING_NOTIFICATION,
  ROOM_EMPTY_RESPONSE,
  ROOM_NOT_FOUND_RESPONSE,
  ONE_USER_RESPONSE,
  NEW_USER_RESPONSE,
  PRIMARY_COLOR_EVENT,
  SECONDARY_COLOR_EVENT
} from '../../constants/socket-constants';
import { SocketServiceInterface } from '../../../domain/interfaces/socket.interface';
import { injectable } from 'inversify';
import { Server, Socket } from 'socket.io';
import { DrawingCommand } from '../../../domain/interfaces/drawing-command.interface';
import { Color } from '@app/domain/models/Color';

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
    this.listenDeleteSelectionCommand(socket);
    this.listenGetUpdatedDrawing(socket);
    this.listenDrawingRequestBroadcastRequest(socket);
    this.listenObjectPrimaryColorChange(socket);
    this.listenObjectSecondaryColorChange(socket);
  }

  private listenObjectPrimaryColorChange(socket:Socket): void {
    socket.on(PRIMARY_COLOR_EVENT, (colorData) => {
      this.sendObjectPrimaryColorChange(socket, colorData);
    });
  }

  private sendObjectPrimaryColorChange(socket:Socket, colorData:Color){
    socket.to(colorData.roomName)
      .emit(PRIMARY_COLOR_EVENT, colorData);
  }

  private listenObjectSecondaryColorChange(socket:Socket): void {
    socket.on(SECONDARY_COLOR_EVENT, (colorData) => {
      this.sendObjectSecondaryColorChange(socket, colorData);
    });
  }

  private sendObjectSecondaryColorChange(socket:Socket, colorData:Color){
    socket.to(colorData.roomName)
      .emit(SECONDARY_COLOR_EVENT, colorData);
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
      console.log(selectionCommand);
      this.emitTransformSelectionCommand(selectionCommand, socket);
    });
  }

  private listenDeleteSelectionCommand(socket: Socket): void {
    socket.on(DELETE_SELECTION_EVENT, (deleteSelectionCommand: any) => {
      this.emitDeleteSelectionCommand(deleteSelectionCommand, socket);
    });
  }

  private listenGetUpdatedDrawing(socket: Socket): void {
    socket.on(UPDATE_DRAWING_EVENT, (roomName:string, callback) => {
      console.log("Request update drawing room: ", roomName);
      const clientIdsInRoomSet = this.namespace.adapter.rooms.get(roomName);

      if(!clientIdsInRoomSet) {
        callback({
          status: ROOM_NOT_FOUND_RESPONSE
        });
        return;
      }

      if (clientIdsInRoomSet.size < 1){
        console.log(`Room ${roomName} is empty`);
        callback({
          status: ROOM_EMPTY_RESPONSE
        });
        return;
      }
      
      let alreadyConnectedUserId;
      for(let clientInRoomSet of clientIdsInRoomSet){
        if (clientInRoomSet != socket.id){
          alreadyConnectedUserId = clientInRoomSet;
          break;
        }
      }

      if(!alreadyConnectedUserId){
        callback({
          status: ONE_USER_RESPONSE
        })
        return
      }

      this.sendUpdateDrawingRequest(socket, alreadyConnectedUserId, socket.id);
      
      callback({
        status: NEW_USER_RESPONSE
      });
    });
  }

  private sendUpdateDrawingRequest(socket:Socket, alreadyConnectedUserId: string, newClientId: string): void {
    let request = {
      newUserId: newClientId,
      alreadyConnectedUserId: alreadyConnectedUserId 
    }
    socket.broadcast.to(alreadyConnectedUserId).emit(UPDATE_DRAWING_EVENT, request);
  }

  private listenDrawingRequestBroadcastRequest(socket: Socket): void {
    socket.on(UPDATE_DRAWING_NOTIFICATION, (newClientId) => {
      this.sendFetchDrawingNotification(socket, newClientId);
    });
  }

  private sendFetchDrawingNotification(socket:Socket, newClientId: string): void {
    socket.broadcast.to(newClientId).emit(FETCH_DRIVING_EVENT);
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

  private emitDeleteSelectionCommand(
    drawingCommand: any,
    socket: Socket,
  ): void {
    socket
      .to(drawingCommand.roomName)
      .emit(DELETE_SELECTION_EVENT, drawingCommand);
  }
}
