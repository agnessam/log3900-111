import { Injectable } from "@angular/core";
import { ICommand } from "../../interfaces/command.interface";
import { SocketTool } from "../tools/socket-tool";
import { CommandFactoryService } from "./factories/command-factory/command-factory.service";

@Injectable({
  providedIn: "root",
})
export class SynchronisationService {
  previewShapes: Map<string, ICommand>;

  constructor(private commandFactory: CommandFactoryService) {}

  execute(drawingCommand: SocketTool) {
    // if (this.previewShapes.has(drawingCommand.drawingCommand.id)) {
    //   let inProgressShape = this.previewShapes.get(drawingCommand.drawingCommand.id);
    //   inProgressShape.update(drawingCommand.drawingCommand);
    // }

    let command: ICommand | null = this.commandFactory.createCommand(
      drawingCommand.type,
      drawingCommand.drawingCommand
    ); // TODO: factory returns command
    if (command) {
      command.execute();
    }
  }
}
