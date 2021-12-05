import { Injectable } from "@angular/core";
import { ICommand } from "../../../../interfaces/command.interface";
import { Point } from "src/app/shared";
import {
  DrawingService,
  RendererProviderService,
} from "src/app/modules/workspace";
import { ResizeCommand } from "./resize-command";
import { DrawingSocketService } from "../../../synchronisation/sockets/drawing-socket/drawing-socket.service";

const DOUBLING_SCALE_MODIFIER = 2;
const SCALE_POSITIONNER_MODIFIER = 1;
const NAV_BAR_Y_OFFSET = 63.98;
@Injectable({
  providedIn: "root",
})
export class ResizeSelectionService {
  private objectList: SVGElement[];

  private resizeCommand: ResizeCommand | null;

  private oldRectBox: ClientRect;
  private lastOffset: Point;

  private ctrlPoint: SVGRectElement | null;
  private ctrlPointList: SVGRectElement[];

  private xFactor: number;

  private xInverser = 1;
  private yInverser = 1;

  private delta: Point = { x: 0, y: 0 };

  isAlt = false;
  isShift = false;

  constructor(
    private rendererService: RendererProviderService,
    private drawingService: DrawingService,
    private drawingSocketService: DrawingSocketService
  ) {}

  setCtrlPointList(ctrlPointList: SVGRectElement[]): void {
    this.ctrlPointList = ctrlPointList;
  }

  createResizeCommand(
    recSelection: SVGPolygonElement,
    objectList: SVGElement[],
    offset: Point,
    ctrlPoint: SVGRectElement | null
  ): void {
    this.delta = { x: 0, y: 0 };
    this.oldRectBox = recSelection.getBoundingClientRect();
    this.ctrlPoint = ctrlPoint;
    this.objectList = objectList;
    this.lastOffset = offset;
    this.xFactor = this.drawingService.drawing.getBoundingClientRect().left;
    this.resizeCommand = new ResizeCommand(
      this.rendererService.renderer,
      this.objectList
    );
  }

  endCommand(): void {
    this.resizeCommand = null;
  }

  hasCommand(): boolean {
    return this.resizeCommand ? true : false;
  }

  getCommand(): ICommand {
    return this.resizeCommand as ResizeCommand;
  }

  resize(deltaX: number, deltaY: number, offset: Point): void {
    if (this.resizeCommand) {
      this.lastOffset = offset;

      this.delta.x += deltaX;
      this.delta.y += deltaY;

      let scaleReturn: IScale = {
        xScale: 1,
        yScale: 1,
        xTranslate: this.oldRectBox.left - this.xFactor,
        yTranslate: this.oldRectBox.top,
      };

      switch (this.ctrlPoint) {
        case this.ctrlPointList[0]: {
          scaleReturn = this.topLeftResize(offset);
          break;
        }
        case this.ctrlPointList[1]: {
          scaleReturn = this.topMiddleResize(scaleReturn);
          break;
        }
        case this.ctrlPointList[2]: {
          scaleReturn = this.topRightResize(offset, scaleReturn.xTranslate);
          break;
        }
        case this.ctrlPointList[3]: {
          scaleReturn = this.middleRightResize(scaleReturn);
          break;
        }
        case this.ctrlPointList[4]: {
          scaleReturn = this.bottomRightResize(
            offset,
            scaleReturn.xTranslate,
            scaleReturn.yTranslate
          );
          break;
        }
        case this.ctrlPointList[5]: {
          scaleReturn = this.bottomMiddleResize(scaleReturn);
          break;
        }
        case this.ctrlPointList[6]: {
          scaleReturn = this.bottomLeftResize(offset, scaleReturn.yTranslate);
          break;
        }
        case this.ctrlPointList[7]: {
          scaleReturn = this.middleLeftResize(scaleReturn);
          break;
        }
      }

      if (this.isAlt) {
        scaleReturn.xScale =
          DOUBLING_SCALE_MODIFIER * scaleReturn.xScale -
          SCALE_POSITIONNER_MODIFIER;
        scaleReturn.xTranslate += (this.xInverser * this.oldRectBox.width) / 2;

        scaleReturn.yScale =
          DOUBLING_SCALE_MODIFIER * scaleReturn.yScale -
          SCALE_POSITIONNER_MODIFIER;
        scaleReturn.yTranslate += (this.yInverser * this.oldRectBox.height) / 2;
      }
      this.resizeCommand.setScales(
        scaleReturn.xScale,
        scaleReturn.yScale,
        scaleReturn.xTranslate,
        scaleReturn.yTranslate
      );

      this.resizeCommand.execute();
      let objectIds: String[] = [];
      this.objectList.forEach((object) => objectIds.push(object.id));
      let resizeSocket = {
        id: objectIds[0],
        xScaled: scaleReturn.xScale,
        yScaled: scaleReturn.yScale,
        xTranslate: scaleReturn.xTranslate,
        yTranslate: scaleReturn.yTranslate,
        previousTransform: this.resizeCommand.getLastTransformation(),
      };
      this.drawingSocketService.sendTransformSelectionCommand(
        resizeSocket,
        "SelectionResize"
      );
    }
  }

  resizeWithLastOffset(): void {
    this.resize(0, 0, this.lastOffset);
  }

  private topLeftResize(offset: Point): IScale {
    let newXScale =
      (this.oldRectBox.width - this.delta.x) / this.oldRectBox.width;
    let newYScale =
      (this.oldRectBox.height - this.delta.y) / this.oldRectBox.height;

    const newXTranslate =
      this.oldRectBox.left + this.oldRectBox.width - this.xFactor;
    const newYTranslate =
      this.oldRectBox.top + this.oldRectBox.height - NAV_BAR_Y_OFFSET;

    this.xInverser = -1;
    this.yInverser = -1;

    return {
      xScale: newXScale,
      yScale: newYScale,
      xTranslate: newXTranslate,
      yTranslate: newYTranslate,
    };
  }

  private topMiddleResize(scale: IScale): IScale {
    const newYScale =
      (this.oldRectBox.height - this.delta.y) / this.oldRectBox.height;

    const newYTranslate =
      this.oldRectBox.top + this.oldRectBox.height - NAV_BAR_Y_OFFSET;

    this.yInverser = -1;
    return {
      xScale: scale.xScale,
      yScale: newYScale,
      xTranslate: scale.xTranslate,
      yTranslate: newYTranslate,
    };
  }

  private topRightResize(offset: Point, oldXTranslate: number): IScale {
    let newXScale =
      (this.oldRectBox.width + this.delta.x) / this.oldRectBox.width;
    let newYScale =
      (this.oldRectBox.height - this.delta.y) / this.oldRectBox.height;

    const newYTranslate =
      this.oldRectBox.top + this.oldRectBox.height - NAV_BAR_Y_OFFSET;

    this.xInverser = 1;
    this.yInverser = -1;

    return {
      xScale: newXScale,
      yScale: newYScale,
      xTranslate: oldXTranslate,
      yTranslate: newYTranslate,
    };
  }

  private middleRightResize(scale: IScale): IScale {
    const newXScale =
      (this.oldRectBox.width + this.delta.x) / this.oldRectBox.width;
    this.xInverser = 1;
    return {
      xScale: newXScale,
      yScale: scale.yScale,
      xTranslate: scale.xTranslate,
      yTranslate: scale.yTranslate,
    };
  }

  private bottomRightResize(
    offset: Point,
    oldXTranslate: number,
    oldYTranslate: number
  ): IScale {
    let newXScale =
      (this.oldRectBox.width + this.delta.x) / this.oldRectBox.width;
    let newYScale =
      (this.oldRectBox.height + this.delta.y) / this.oldRectBox.height;

    this.xInverser = 1;
    this.yInverser = 1;

    return {
      xScale: newXScale,
      yScale: newYScale,
      xTranslate: oldXTranslate,
      yTranslate: oldYTranslate - NAV_BAR_Y_OFFSET,
    };
  }

  private bottomMiddleResize(scale: IScale): IScale {
    const newYScale =
      (this.oldRectBox.height + this.delta.y) / this.oldRectBox.height;
    this.yInverser = 1;
    return {
      xScale: scale.xScale,
      yScale: newYScale,
      xTranslate: scale.xTranslate,
      yTranslate: scale.yTranslate - NAV_BAR_Y_OFFSET,
    };
  }

  private bottomLeftResize(offset: Point, oldYTranslate: number): IScale {
    let newXScale =
      (this.oldRectBox.width - this.delta.x) / this.oldRectBox.width;
    let newYScale =
      (this.oldRectBox.height + this.delta.y) / this.oldRectBox.height;

    const newXTranslate =
      this.oldRectBox.left + this.oldRectBox.width - this.xFactor;

    this.xInverser = -1;
    this.yInverser = 1;

    return {
      xScale: newXScale,
      yScale: newYScale,
      xTranslate: newXTranslate,
      yTranslate: oldYTranslate - NAV_BAR_Y_OFFSET,
    };
  }

  private middleLeftResize(scale: IScale): IScale {
    const newXScale =
      (this.oldRectBox.width - this.delta.x) / this.oldRectBox.width;

    const newXTranslate =
      this.oldRectBox.left + this.oldRectBox.width - this.xFactor;

    this.xInverser = -1;
    return {
      xScale: newXScale,
      yScale: scale.yScale,
      xTranslate: newXTranslate,
      yTranslate: scale.yTranslate,
    };
  }
}

interface IScale {
  xScale: number;
  yScale: number;
  xTranslate: number;
  yTranslate: number;
}
