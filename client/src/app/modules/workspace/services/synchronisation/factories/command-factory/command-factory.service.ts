import { Injectable } from "@angular/core";
import { DrawingService } from "../../../drawing/drawing.service";
import { ICommand } from "src/app/modules/workspace/interfaces/command.interface";
import {
  DeleteCommand,
  EllipseCommand,
  EraserCommand,
  PencilCommand,
  RectangleCommand,
  RendererProviderService,
} from "src/app/modules/workspace";
import { Pencil } from "../../../tools/pencil-tool/pencil.model";
import { Rectangle } from "../../../tools/tool-rectangle/rectangle.model";
import { Ellipse } from "../../../tools/tool-ellipse/ellipse.model";
import { SelectionStartCommand } from "../../../tools/selection-tool/start-command/selection-start-command.service";
@Injectable({
  providedIn: "root",
})
export class CommandFactoryService {
  constructor(
    private drawingService: DrawingService,
    private rendererService: RendererProviderService
  ) {}

  createCommand(commandType: string, commandParameters: any): ICommand {
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
      case "Eraser":
        let itemsToDelete = new Map<string, SVGElement>();
        for (const id of <string[]>commandParameters) {
          const svgElement = this.drawingService.getObject(id);
          if (svgElement) {
            itemsToDelete.set(id, svgElement);
          }
        }
        return new EraserCommand(itemsToDelete, this.drawingService);
      case "SelectionStart":
        const selectedShapeId = commandParameters.id;
        const selectedShape = this.drawingService.getObject(selectedShapeId);
        if (selectedShape == undefined) {
          throw new Error("Could not find current shape");
        }
        return new SelectionStartCommand(selectedShape);
      case "Delete":
        const id = commandParameters.id;
        const deletedShape = this.drawingService.getObject(id);
        if (deletedShape == undefined)
          throw new Error("Couldn't find the shape you wanted to delete.");
        return new DeleteCommand(this.drawingService, deletedShape);
      default:
        throw new Error("Unable to create command");
    }
  }
}
