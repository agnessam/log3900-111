import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MaterialModules } from "../../app-material.module";
import { WorkspaceComponent } from "./workspace.component";
import { CanvasModule } from "../canvas/canvas.module";

@NgModule({
  exports: [WorkspaceComponent],
  declarations: [WorkspaceComponent],
  imports: [CanvasModule, CommonModule, MaterialModules],
})
export class WorkspaceModule {}
