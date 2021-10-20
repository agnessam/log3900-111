import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MaterialModules } from "../../app-material.module";
import { WorkspaceComponent } from "./workspace.component";
import { CanvasComponent } from "./components/canvas/canvas.component";
@NgModule({
  exports: [WorkspaceComponent],
  declarations: [WorkspaceComponent, CanvasComponent],
  imports: [CommonModule, MaterialModules],
})
export class WorkspaceModule {}
