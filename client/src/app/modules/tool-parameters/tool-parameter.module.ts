import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { MaterialModules } from "../../app-material.module";
import { EllipseToolParameterComponent } from "./components/ellipse-tool-parameter/ellipse-tool-parameter.component";
import { PencilToolParameterComponent } from "./components/pencil-tool-parameter/pencil-tool-parameter.component";
import { RectangleToolParameterComponent } from "./components/rectangle-tool-parameter/rectangle-tool-parameter.component";
import { SelectionToolParameterComponent } from "./components/selection-tool-parameter/selection-tool-parameter.component";
import { WorkspaceModule } from "src/app/modules/workspace/workspace.module";

@NgModule({
  declarations: [
    PencilToolParameterComponent,
    RectangleToolParameterComponent,
    EllipseToolParameterComponent,
    SelectionToolParameterComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModules,
    FontAwesomeModule,
    WorkspaceModule,
  ],
  exports: [PencilToolParameterComponent],
  entryComponents: [
    PencilToolParameterComponent,
    RectangleToolParameterComponent,
    EllipseToolParameterComponent,
    SelectionToolParameterComponent,
  ],
})
export class ToolParameterModule {}
