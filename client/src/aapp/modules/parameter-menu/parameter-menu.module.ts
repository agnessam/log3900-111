import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MaterialModules } from "../../app-material.module";
import { ParameterMenuComponent } from "./parameter-menu.component";
import { WorkspaceModule } from "../workspace/workspace.module";
import { ToolParameterModule } from "../tool-parameters/tool-parameter.module";

@NgModule({
  declarations: [ParameterMenuComponent],
  imports: [
    CommonModule,
    MaterialModules,
    ToolParameterModule,
    WorkspaceModule,
  ],
})
export class ParameterMenuModule {}
