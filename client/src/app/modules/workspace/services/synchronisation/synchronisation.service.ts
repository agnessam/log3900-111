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

  removeFromPreview(id:string): boolean{
    if(this.previewShapes.has(id)){
      this.previewShapes.delete(id);
      return true;
    }
    return false;
  }

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

    console.log(this.previewShapes);
  }

  erase(eraseCommandData: any) {
    let eraseCommand = this.commandFactory.createCommand(
      eraseCommandData.type,
      eraseCommandData.itemsToDeleteIds
    );
    eraseCommand.execute();
  }

  // When starting a selection we simply add it to the list of preview shapes so that
  // no one can manipulate it.
  startSelection(selectionCommandData: SocketTool) {
    let selectionCommand = this.commandFactory.createCommand(
      selectionCommandData.type,
      selectionCommandData.drawingCommand
    );
    this.previewShapes.set(
      selectionCommandData.drawingCommand.id,
      selectionCommand
    );

    console.log(this.previewShapes);
  }

  confirmSelection(confirmSelectionData: SocketTool) {
    console.log(this.previewShapes);
    this.previewShapes.delete(confirmSelectionData.drawingCommand.id);
    console.log(this.previewShapes);
  }
}
