import { Component } from "@angular/core";
import { CommandInvokerService } from "src/app/modules/workspace";
import { DrawingService } from "src/app/modules/workspace";

/// Component pour afficher les options fichiers
@Component({
  selector: "app-control-menu",
  templateUrl: "./control-menu.component.html",
  styleUrls: ["./control-menu.component.scss"],
})
export class ControlMenuComponent {
  constructor(
    private drawingService: DrawingService,
    private commandInvoker: CommandInvokerService
  ) {}

  get isSaved(): boolean {
    return this.drawingService.isSaved;
  }
  get isCreated(): boolean {
    return this.drawingService.isCreated;
  }

  get canUndo(): boolean {
    return this.commandInvoker.canUndo;
  }

  get canRedo(): boolean {
    return this.commandInvoker.canRedo;
  }

  /// Undo
  undo(): void {
    this.commandInvoker.undo();
  }

  /// Redo
  redo(): void {
    this.commandInvoker.redo();
  }
}
