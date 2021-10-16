import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToolsColorComponent } from './tools-color.component';
import { ToolsColorPickerComponent } from './tools-color-picker/tools-color-picker.component';

@NgModule({
  declarations: [ToolsColorComponent, ToolsColorPickerComponent],
  imports: [
    CommonModule
  ]
})
export class ToolsColorModule { }
