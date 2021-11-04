import { Injectable } from "@angular/core";
import { DrawingService } from "../../../drawing/drawing.service";
import { ICommand } from "src/app/modules/workspace/interfaces/command.interface";
import {
  EllipseCommand,
  PencilCommand,
  RectangleCommand,
  RendererProviderService,
} from "src/app/modules/workspace";
import { Tool } from "../../../tools/tool.model";
import { Pencil } from "../../../tools/pencil-tool/pencil.model";
import { Rectangle } from "../../../tools/tool-rectangle/rectangle.model";
import { Ellipse } from "../../../tools/tool-ellipse/ellipse.model";
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
      case "Rectangle":
        return new RectangleCommand(
          this.rendererService.renderer,
          commandParameters as Rectangle,
          this.drawingService
        );
      case "Ellipse":
        return new EllipseCommand(
          this.rendererService.renderer,
          commandParameters as Ellipse,
          this.drawingService
        );
      default:
        throw new Error("Unable to create command");
    }
  }
}
