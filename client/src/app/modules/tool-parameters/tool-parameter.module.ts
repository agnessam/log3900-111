import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { FontAwesomeModule } from "@fortawesome/angular-fontawesome";
import { MaterialModules } from "../../app-material.module";
import { ApplierToolParameterComponent } from "./components/applier-tool-parameter/applier-tool-parameter.component";
import { BrushToolParameterComponent } from "./components/brush-tool-parameter/brush-tool-parameter.component";
import { BucketFillToolParameterComponent } from "./components/bucket-fill-tool-parameter/bucket-fill-tool-parameter.component";
import { EllipseToolParameterComponent } from "./components/ellipse-tool-parameter/ellipse-tool-parameter.component";
import { EraserToolParameterComponent } from "./components/eraser-tool-parameter/eraser-tool-parameter.component";
import { FeatherToolParameterComponent } from "./components/feather-tool-parameter/feather-tool-parameter.component";
import { GridParameterComponent } from "./components/grid-parameter/grid-parameter.component";
import { LineToolParameterComponent } from "./components/line-tool-parameter/line-tool-parameter.component";
import { PencilToolParameterComponent } from "./components/pencil-tool-parameter/pencil-tool-parameter.component";
import { PipetteToolParameterComponent } from "./components/pipette-tool-parameter/pipette-tool-parameter.component";
import { PolygonToolParameterComponent } from "./components/polygon-tool-parameter/polygon-tool-parameter.component";
import { RectangleToolParameterComponent } from "./components/rectangle-tool-parameter/rectangle-tool-parameter.component";
import { SelectionToolParameterComponent } from "./components/selection-tool-parameter/selection-tool-parameter.component";
import { SprayToolParameterComponent } from "./components/spray-tool-parameter/spray-tool-parameter.component";
import { StampToolParameterComponent } from "./components/stamp-tool-parameter/stamp-tool-parameter.component";
import { TextToolParameterComponent } from "./components/text-tool-parameter/text-tool-parameter.component";
import { WorkspaceModule } from "src/app/modules/workspace/workspace.module";

@NgModule({
  declarations: [
    PencilToolParameterComponent,
    RectangleToolParameterComponent,
    BrushToolParameterComponent,
    ApplierToolParameterComponent,
    EllipseToolParameterComponent,
    PipetteToolParameterComponent,
    StampToolParameterComponent,
    PolygonToolParameterComponent,
    GridParameterComponent,
    LineToolParameterComponent,
    SelectionToolParameterComponent,
    EraserToolParameterComponent,
    TextToolParameterComponent,
    FeatherToolParameterComponent,
    BucketFillToolParameterComponent,
    SprayToolParameterComponent,
  ],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MaterialModules,
    FontAwesomeModule,
    WorkspaceModule,
  ],
  exports: [BrushToolParameterComponent, PencilToolParameterComponent],
  entryComponents: [
    BrushToolParameterComponent,
    PencilToolParameterComponent,
    RectangleToolParameterComponent,
    ApplierToolParameterComponent,
    EllipseToolParameterComponent,
    PipetteToolParameterComponent,
    StampToolParameterComponent,
    PolygonToolParameterComponent,
    GridParameterComponent,
    LineToolParameterComponent,
    SelectionToolParameterComponent,
    EraserToolParameterComponent,
    TextToolParameterComponent,
    FeatherToolParameterComponent,
    BucketFillToolParameterComponent,
    SprayToolParameterComponent,
  ],
})
export class ToolParameterModule {}
