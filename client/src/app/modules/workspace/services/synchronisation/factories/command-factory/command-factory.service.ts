import { Injectable } from "@angular/core";
import { DrawingService } from "../../../drawing/drawing.service";
import { ICommand } from "src/app/modules/workspace/interfaces/command.interface";
import {
  PencilCommand,
  RendererProviderService,
} from "src/app/modules/workspace";
import { Tool } from "../../../tools/tool.model";
import { Pencil } from "../../../tools/pencil-tool/pencil.model";

@Injectable({
  providedIn: "root",
})
export class CommandFactoryService {
  constructor(
    private drawingService: DrawingService,
    private rendererService: RendererProviderService
  ) {}

  createCommand(commandType: string, commandParameters: Tool): ICommand {
    switch (commandType) {
      case "Pencil":
        return new PencilCommand(
          this.rendererService.renderer,
          commandParameters as Pencil,
          this.drawingService
        );
      default:
        throw new Error("Unable to create command");
    }
  }
}
