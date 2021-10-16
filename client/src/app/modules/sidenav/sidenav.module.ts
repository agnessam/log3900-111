import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { ToolParameterModule } from "../tool-parameters/tool-parameter.module";
import {
  BrushToolParameterComponent,
  PencilToolParameterComponent,
} from "src/app/modules/tool-parameters";
import { ParameterMenuModule } from "../parameter-menu/parameter-menu.module";

@NgModule({
  imports: [CommonModule, ParameterMenuModule, ToolParameterModule],
  declarations: [],
  entryComponents: [PencilToolParameterComponent, BrushToolParameterComponent],
})
export class SidenavModule {}
