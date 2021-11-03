import { Injectable } from "@angular/core";
import { ICommand } from "../../interfaces/command.interface";
import { SocketTool } from "../tools/socket-tool";
import { CommandFactoryService } from "./factories/command-factory/command-factory.service";

@Injectable({
  providedIn: "root",
})
export class SynchronisationService {
  previewShapes: Map<string, ICommand> = new Map<string, ICommand>();

  constructor(private commandFactory: CommandFactoryService) {}

  draw(drawingCommand: SocketTool) {
    const commandId = drawingCommand.drawingCommand.id;
    let command: ICommand | undefined;

    if (this.previewShapes.has(commandId)) {
      command = this.previewShapes.get(commandId);
      command!.update(drawingCommand.drawingCommand);
    } else {
      command = this.commandFactory.createCommand(
        drawingCommand.type,
        drawingCommand.drawingCommand
      );
    }

    if (command) {
      this.previewShapes.set(commandId, command);
      command.execute();
    }
  }
}
