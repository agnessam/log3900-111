import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NewDrawingComponent } from "./new-drawing.component";
import { NewDrawingAlertComponent } from "./new-drawing-alert/new-drawing-alert.component";
import { NewDrawingFormComponent} from "./new-drawing-form/new-drawing-form.component";


@NgModule({
  declarations: [NewDrawingComponent, NewDrawingAlertComponent, NewDrawingFormComponent],
  imports: [CommonModule]
})
export class NewDrawingModule { }
