import { MuseumComponent } from "./museum.component";
import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MuseumRoutingModule } from "./museum-routing.module";
import { MatIconModule } from "@angular/material/icon";
import { MuseumDialog } from "./museum-dialog/museum-dialog.component";
import { AvatarModule } from "ngx-avatar";
import { CardsModule } from "../cards/cards.module";

@NgModule({
  declarations: [MuseumComponent, MuseumDialog],
  imports: [CommonModule,
     MuseumRoutingModule,
     MatIconModule,
     AvatarModule,
     CardsModule],
})
export class MuseumModule {}
