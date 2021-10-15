import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MaterialModules } from "../../app-material.module";
import { WorkspaceComponent } from "./workspace.component";

@NgModule({
  declarations: [WorkspaceComponent],
  imports: [CommonModule, MaterialModules],
})
export class WorkspaceModule {}
