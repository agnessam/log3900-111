import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MaterialModules } from "../../app-material.module";
import { WorkspaceComponent } from "./workspace.component";
import { CanvasComponent } from "./components/canvas/canvas.component";
import { ControlMenuComponent } from "../parameter-menu/components/control-menu/control-menu.component";

@NgModule({
  exports: [WorkspaceComponent, ControlMenuComponent],
  declarations: [WorkspaceComponent, CanvasComponent],
  imports: [CommonModule, MaterialModules],
})
export class WorkspaceModule {}
