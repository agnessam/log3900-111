import { MuseumComponent } from "./museum.component";
import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MuseumRoutingModule } from "./museum-routing.module";
import { MatIconModule } from "@angular/material/icon";
import { MuseumDialog } from "./museum-dialog/museum-dialog.component";
import { AvatarModule } from "ngx-avatar";
import { CardsModule } from "../cards/cards.module";
import { MatFormFieldModule } from "@angular/material/form-field";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { MatDialogModule } from "@angular/material/dialog";
import { MatInputModule } from "@angular/material/input";
import { MatButtonModule } from "@angular/material/button";

@NgModule({
  declarations: [MuseumComponent, MuseumDialog],
  imports: [
    CommonModule,
    MuseumRoutingModule,
    MatIconModule,
    AvatarModule,
    CardsModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    FormsModule,
    MatDialogModule,
    MatInputModule,
    MatButtonModule,
  ],
})
export class MuseumModule {}
