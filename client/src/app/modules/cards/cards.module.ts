import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MatCardModule } from "@angular/material/card";
import { DrawingCardComponent } from "./drawing-card/drawing-card.component";

@NgModule({
  declarations: [DrawingCardComponent],
  imports: [CommonModule, MatCardModule],
  exports: [DrawingCardComponent],
})
export class CardsModule {}
