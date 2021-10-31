import { Injectable } from "@angular/core";
//import { Message } from "../models/message.model";
import { EventEmitter } from "@angular/core";

@Injectable({
  providedIn: "root",
})

export class ChatService{

  constructor() {}

  toggleChatOverlay : EventEmitter<string> = new EventEmitter<string>()
  toggleCanalOverlay : EventEmitter<boolean> = new EventEmitter<boolean>()

}
