import { Injectable } from "@angular/core";
import { CommandFactoryService } from "./factories/command-factory/command-factory.service";
import { ICommand } from "../../interfaces/command.interface";
import { SocketTool } from "../tools/socket-tool";

@Injectable({
  providedIn: "root",
})
export class SynchronisationService {
  constructor(private commandFactory: CommandFactoryService) {}

  execute(drawingCommand: SocketTool) {
    let command: ICommand | null = this.commandFactory.createCommand(drawingCommand.type, drawingCommand.drawingCommand); // TODO: factory returns command
    if(command){
      command.execute();
    }
  }
}
