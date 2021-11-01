import { Injectable } from "@angular/core";
//import { Message } from "../models/message.model";
import { EventEmitter } from "@angular/core";
import { TextChannel } from '../models/text-channel.model';

@Injectable({
  providedIn: "root",
})

export class ChatService{

  constructor() {}

  toggleChatOverlay : EventEmitter<TextChannel> = new EventEmitter<TextChannel>()
  toggleChannelOverlay : EventEmitter<null> = new EventEmitter<null>()

}
