import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { MatCardModule } from "@angular/material/card";
import { MatIconModule } from "@angular/material/icon";
import { AvatarModule } from "ngx-avatar";
import { DrawingCardComponent } from "./drawing-card/drawing-card.component";

@NgModule({
  declarations: [DrawingCardComponent],
  imports: [CommonModule, MatCardModule, AvatarModule, MatIconModule],
  exports: [DrawingCardComponent],
})
export class CardsModule {}
