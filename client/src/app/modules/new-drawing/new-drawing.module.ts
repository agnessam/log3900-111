import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { NewDrawingComponent } from "./new-drawing.component";
import { NewDrawingAlertComponent } from "./new-drawing-alert/new-drawing-alert.component";
import { NewDrawingFormComponent } from "./new-drawing-form/new-drawing-form.component";
import { SharedModule } from "src/app/shared/shared.module";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { ColorPickerModule } from "src/app/modules/color-picker/color-picker.module";
@NgModule({
  declarations: [
    NewDrawingComponent,
    NewDrawingAlertComponent,
    NewDrawingFormComponent,
  ],
  imports: [
    CommonModule,
    SharedModule,
    FormsModule,
    ReactiveFormsModule,
    ColorPickerModule,
  ],
})
export class NewDrawingModule {}
