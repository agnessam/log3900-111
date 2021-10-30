import { Injectable } from "@angular/core";
//import { Message } from "../models/message.model";
import { EventEmitter } from "@angular/core";

@Injectable({
  providedIn: "root",
})

export class ChatService{

  constructor() {}

  toggle : EventEmitter<boolean> = new EventEmitter<boolean>()

}
