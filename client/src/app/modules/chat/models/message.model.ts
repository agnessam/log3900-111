export interface Message {
  _id?: string;
  message: string;
  timestamp: string;
  author: string;
  _roomId?: string;
  roomName: string;
}
