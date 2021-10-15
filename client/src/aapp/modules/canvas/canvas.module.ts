import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { CanvasComponent } from "./canvas.component";

@NgModule({
  exports: [CanvasComponent],
  declarations: [CanvasComponent],
  imports: [CommonModule],
})
export class CanvasModule {}
