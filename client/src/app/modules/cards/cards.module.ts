import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { ReactiveFormsModule } from "@angular/forms";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { MatDialogModule } from "@angular/material/dialog";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatIconModule } from "@angular/material/icon";
import { MatInputModule } from "@angular/material/input";
import { AvatarModule } from "ngx-avatar";
import { DrawingCardComponent } from "./drawing-card/drawing-card.component";
import { JoinDrawingDialogComponent } from "./drawing-card/join-drawing-dialog/join-drawing-dialog.component";

@NgModule({
  declarations: [DrawingCardComponent, JoinDrawingDialogComponent],
  imports: [
    CommonModule,
    MatCardModule,
    AvatarModule,
    MatIconModule,
    MatDialogModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
  ],
  exports: [DrawingCardComponent],
})
export class CardsModule {}
