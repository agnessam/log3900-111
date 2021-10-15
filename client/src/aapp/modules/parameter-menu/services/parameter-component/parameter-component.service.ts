import { Injectable, Type } from "@angular/core";
import { ControlMenuComponent } from "src/app/components/control-menu/control-menu.component";
import { ApplierToolParameterComponent } from "../../../tool-parameters/components/applier-tool-parameter/applier-tool-parameter.component";
import { BrushToolParameterComponent } from "../../../tool-parameters/components/brush-tool-parameter/brush-tool-parameter.component";
import { BucketFillToolParameterComponent } from "../../../tool-parameters/components/bucket-fill-tool-parameter/bucket-fill-tool-parameter.component";
import { EllipseToolParameterComponent } from "../../../tool-parameters/components/ellipse-tool-parameter/ellipse-tool-parameter.component";
import { EraserToolParameterComponent } from "../../../tool-parameters/components/eraser-tool-parameter/eraser-tool-parameter.component";
import { FeatherToolParameterComponent } from "../../../tool-parameters/components/feather-tool-parameter/feather-tool-parameter.component";
import { GridParameterComponent } from "../../../tool-parameters/components/grid-parameter/grid-parameter.component";
import { LineToolParameterComponent } from "../../../tool-parameters/components/line-tool-parameter/line-tool-parameter.component";
import { PenToolParameterComponent } from "../../../tool-parameters/components/pen-tool-parameter/pen-tool-parameter.component";
import { PencilToolParameterComponent } from "../../../tool-parameters/components/pencil-tool-parameter/pencil-tool-parameter.component";
import { PipetteToolParameterComponent } from "../../../tool-parameters/components/pipette-tool-parameter/pipette-tool-parameter.component";
import { PolygonToolParameterComponent } from "../../../tool-parameters/components/polygon-tool-parameter/polygon-tool-parameter.component";
import { RectangleToolParameterComponent } from "../../../tool-parameters/components/rectangle-tool-parameter/rectangle-tool-parameter.component";
import { SelectionToolParameterComponent } from "../../../tool-parameters/components/selection-tool-parameter/selection-tool-parameter.component";
import { SprayToolParameterComponent } from "../../../tool-parameters/components/spray-tool-parameter/spray-tool-parameter.component";
import { StampToolParameterComponent } from "../../../tool-parameters/components/stamp-tool-parameter/stamp-tool-parameter.component";
import { TextToolParameterComponent } from "../../../tool-parameters/components/text-tool-parameter/text-tool-parameter.component";

/// Classe permettant d'offrir dyamiquement des component selon un index
@Injectable({
  providedIn: "root",
})
export class ParameterComponentService {
  private parameterComponentList: Type<any>[] = [];
  constructor() {
    this.parameterComponentList.push(
      SelectionToolParameterComponent,
      EraserToolParameterComponent,
      PencilToolParameterComponent,
      BrushToolParameterComponent,
      FeatherToolParameterComponent,
      PenToolParameterComponent,
      RectangleToolParameterComponent,
      EllipseToolParameterComponent,
      PolygonToolParameterComponent,
      LineToolParameterComponent,
      StampToolParameterComponent,
      SprayToolParameterComponent,
      TextToolParameterComponent,
      ApplierToolParameterComponent,
      BucketFillToolParameterComponent,
      PipetteToolParameterComponent
    );
    // Le push se fait par la suite pour s'assurer qu'il s'agit de la derniere classe
    this.parameterComponentList.push(GridParameterComponent);
    this.parameterComponentList.push(ControlMenuComponent);
  }

  /// Retourne le parameterComponent de l'index donner
  getComponent(index: number): Type<any> {
    return this.parameterComponentList[index];
  }
}
