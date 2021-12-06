import { Injectable } from "@angular/core";
import { Color, LineWidth } from "src/app/shared";
import { DrawingService, RendererProviderService } from "../..";
import { ICommand } from "../../interfaces/command.interface";
import { SocketTool } from "../tools/socket-tool";
import { CommandFactoryService } from "./factories/command-factory/command-factory.service";

@Injectable({
  providedIn: "root",
})
export class SynchronisationService {
  previewShapes: Map<string, ICommand> = new Map<string, ICommand>();
  rectangleSelections: Map<string, RectSVGShapePair> = new Map();
  constructor(
    private commandFactory: CommandFactoryService,
    private rendererService: RendererProviderService,
    private drawingService: DrawingService
  ) {}

  private removePreviewBox(id: string) {
    if (this.rectangleSelections.get(id) != undefined) {
      this.rendererService.renderer.removeChild(
        this.drawingService.drawing,
        this.rectangleSelections.get(id)?.rectSelection
      );
    }
    this.rectangleSelections.delete(id);
  }

  removeFromPreview(id: string): boolean {
    if (this.previewShapes.has(id)) {
      this.removePreviewBox(id);
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

    this.resetPreviewBox(selectionCommandData.drawingCommand.id);
  }

  confirmSelection(confirmSelectionData: SocketTool) {
    this.removeFromPreview(confirmSelectionData.drawingCommand.id);
  }

  transformSelection(transformSelectionData: SocketTool) {
    const commandId = transformSelectionData.drawingCommand.id;
    let command: ICommand | undefined;
    let transformationCommand: ICommand;
    if (this.previewShapes.has(commandId)) {
      command = this.previewShapes.get(commandId);

      transformationCommand = this.commandFactory.createCommand(
        transformSelectionData.type,
        transformSelectionData.drawingCommand
      );

      this.resetPreviewBox(commandId);

      if (transformationCommand instanceof command!.constructor) {
        command!.update(transformSelectionData.drawingCommand);
      } else {
        this.previewShapes.set(commandId, transformationCommand);
      }
    }

    this.previewShapes.get(commandId)!.execute();
  }

  private resetPreviewBox(commandId: string) {
    if (this.rectangleSelections.has(commandId)) {
      this.rendererService.renderer.removeChild(
        this.drawingService.drawing,
        this.rectangleSelections.get(commandId)?.rectSelection
      );
    }

    // Set rect for object
    let selectedObject = this.drawingService.getObject(commandId);
    if (
      !this.rectangleSelections.has(commandId) &&
      selectedObject != undefined
    ) {
      let rectSVGShapePair: RectSVGShapePair = {
        rectSelection: this.rendererService.renderer.createElement(
          "rect",
          "svg"
        ),
        selectedShape: selectedObject,
      };
      this.rectangleSelections.set(commandId, rectSVGShapePair);
    }
    let rectangleSelection = this.rectangleSelections.get(commandId);
    if (rectangleSelection != undefined) {
      this.setRectSelection(rectangleSelection);
    }
  }

  deleteSelection(deleteSelectionData: SocketTool): void {
    let transformCommand = this.commandFactory.createCommand(
      deleteSelectionData.type,
      deleteSelectionData.drawingCommand
    );
    transformCommand.execute();

    this.removePreviewBox(deleteSelectionData.drawingCommand.id);
  }

  setSelectionLineWidth(lineWidthData: LineWidth): void {
    let lineWidthCommand = this.commandFactory.createCommand(
      "LineWidth",
      lineWidthData
    );
    lineWidthCommand.execute();
  }

  setObjectPrimaryColor(colorData: Color): void {
    const primaryColorCommand = this.commandFactory.createCommand(
      "PrimaryColor",
      colorData
    );
    primaryColorCommand.execute();
  }

  setObjectSecondaryColor(colorData: Color): void {
    const secondaryColorCommand = this.commandFactory.createCommand(
      "SecondaryColor",
      colorData
    );
    secondaryColorCommand.execute();
  }

  private setRectSelection(rectSVGShapePair: RectSVGShapePair): void {
    let boundingRect: DOMRect =
      rectSVGShapePair.selectedShape.getBoundingClientRect();

    this.rendererService.renderer.setAttribute(
      rectSVGShapePair.rectSelection,
      "class",
      "preview"
    );
    this.rendererService.renderer.setAttribute(
      rectSVGShapePair.rectSelection,
      "x",
      `${
        boundingRect.left -
        (this.drawingService.drawing as SVGSVGElement).getBoundingClientRect()
          .left
      }`
    );
    this.rendererService.renderer.setAttribute(
      rectSVGShapePair.rectSelection,
      "y",
      `${boundingRect.top - 64}`
    );
    this.rendererService.renderer.setAttribute(
      rectSVGShapePair.rectSelection,
      "width",
      `${boundingRect.width}`
    );
    this.rendererService.renderer.setAttribute(
      rectSVGShapePair.rectSelection,
      "height",
      `${boundingRect.height}`
    );
    this.rendererService.renderer.setStyle(
      rectSVGShapePair.rectSelection,
      "stroke",
      `rgba(0, 0, 255, 0.5)`
    );
    this.rendererService.renderer.setStyle(
      rectSVGShapePair.rectSelection,
      "stroke-width",
      `10`
    );
    this.rendererService.renderer.setStyle(
      rectSVGShapePair.rectSelection,
      "stroke-dasharray",
      `10,10`
    );
    this.rendererService.renderer.setStyle(
      rectSVGShapePair.rectSelection,
      "fill",
      `none`
    );
    this.rendererService.renderer.setAttribute(
      rectSVGShapePair.rectSelection,
      "pointer-events",
      "none"
    );
    this.rendererService.renderer.appendChild(
      this.drawingService.drawing,
      rectSVGShapePair.rectSelection
    );
  }
}

interface RectSVGShapePair {
  rectSelection: SVGRectElement;
  selectedShape: SVGElement;
}
