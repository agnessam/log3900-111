import { Injectable } from "@angular/core";
import { ICommand } from "../../../../interfaces/command.interface";

@Injectable({
  providedIn: "root",
})
export class SelectionStartCommandService implements ICommand {
  constructor() {}

  update(drawingCommand: any): void {
    throw new Error("Method not implemented.");
  }
  undo(): void {
    throw new Error("Method not implemented.");
  }
  execute(): void {
    throw new Error("Method not implemented.");
  }
}
