import { Injectable } from "@angular/core";
import { ICommand } from "../../interfaces/command.interface";
import { ResizeCommand } from "../tools/selection-tool/resize-command/resize-command";
import { SocketTool } from "../tools/socket-tool";
import { CommandFactoryService } from "./factories/command-factory/command-factory.service";

@Injectable({
  providedIn: "root",
})
export class SynchronisationService {
  previewShapes: Map<string, ICommand> = new Map<string, ICommand>();

  constructor(private commandFactory: CommandFactoryService) {}

  removeFromPreview(id: string): boolean {
    if (this.previewShapes.has(id)) {
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
  }

  confirmSelection(confirmSelectionData: SocketTool) {
    this.previewShapes.delete(confirmSelectionData.drawingCommand.id);
  }

  transformSelection(transformSelectionData: SocketTool) {
    const commandId = transformSelectionData.drawingCommand.id;
    let command: ICommand | undefined;
    if (this.previewShapes.has(commandId)) {
      command = this.previewShapes.get(commandId);
      if (command! instanceof ResizeCommand) {
        command!.update(transformSelectionData.drawingCommand);
      } else {
        command = this.commandFactory.createCommand(
          transformSelectionData.type,
          transformSelectionData.drawingCommand
        );
        this.previewShapes.set(commandId, command);
      }
    }
    command!.execute();
  }
}
