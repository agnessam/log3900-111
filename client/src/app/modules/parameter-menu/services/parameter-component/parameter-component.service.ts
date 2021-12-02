import { Injectable, Type } from "@angular/core";
import {
  EllipseToolParameterComponent,
  PencilToolParameterComponent,
  RectangleToolParameterComponent,
  SelectionToolParameterComponent,
} from "src/app/modules/tool-parameters";

/// Classe permettant d'offrir dyamiquement des component selon un index
@Injectable({
  providedIn: "root",
})
export class ParameterComponentService {
  private parameterComponentList: Type<any>[] = [];
  constructor() {
    this.parameterComponentList.push(
      SelectionToolParameterComponent,
      PencilToolParameterComponent,
      RectangleToolParameterComponent,
      EllipseToolParameterComponent
    );
  }

  /// Retourne le parameterComponent de l'index donner
  getComponent(index: number): Type<any> {
    return this.parameterComponentList[index];
  }
}
