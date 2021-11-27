export interface Message {
  _id?: string;
  message: string;
  timestamp: Date;
  author: string;
  _roomId?: string;
  roomName: string;
}
