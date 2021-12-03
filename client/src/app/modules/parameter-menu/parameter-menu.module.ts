import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MaterialModules } from "../../app-material.module";
import { ParameterMenuComponent } from "./parameter-menu.component";
import { WorkspaceModule } from "../workspace/workspace.module";
import { ToolParameterModule } from "../tool-parameters/tool-parameter.module";
import { ParameterDirective } from "./directives/parameter.directive";
import { ToolsColorModule } from "../tools-color/tools-color.module";

@NgModule({
  providers: [],
  declarations: [ParameterMenuComponent, ParameterDirective],
  imports: [
    CommonModule,
    MaterialModules,
    ToolParameterModule,
    WorkspaceModule,
    ToolsColorModule,
  ],
  exports: [ParameterMenuComponent],
})
export class ParameterMenuModule {}
