import { MuseumComponent } from "./museum.component";
import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MuseumRoutingModule } from "./museum-routing.module";
import { MatIconModule } from "@angular/material/icon";
import { MuseumDialog } from "./museum-dialog/museum-dialog.component";
import { AvatarModule } from "ngx-avatar";
import { CardsModule } from "../cards/cards.module";
import { MatCardModule } from "@angular/material/card";
import { MatDialogModule } from "@angular/material/dialog";
import { MatButtonModule } from "@angular/material/button";

@NgModule({
  declarations: [MuseumComponent, MuseumDialog],
  imports: [
    CommonModule,
    MuseumRoutingModule,
    MatIconModule,
    AvatarModule,
    CardsModule,
    MatCardModule,
    MatDialogModule,
    MatButtonModule,
  ],
})
export class MuseumModule {}
