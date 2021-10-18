import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { ToolsColorComponent } from "./tools-color.component";
import { ToolsColorPickerComponent } from "./tools-color-picker/tools-color-picker.component";
import { SharedModule } from "src/app/shared/shared.module";
import { ColorPickerModule } from "../color-picker/color-picker.module";
import { ReactiveFormsModule } from "@angular/forms";

@NgModule({
  declarations: [ToolsColorComponent, ToolsColorPickerComponent],
  imports: [CommonModule, SharedModule, ColorPickerModule, ReactiveFormsModule],
  exports: [ToolsColorComponent],
})
export class ToolsColorModule {}
