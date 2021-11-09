import { MuseumComponent } from './museum.component';
import { NgModule } from '@angular/core';
import { CommonModule } from "@angular/common";
import { MuseumRoutingModule } from './museum-routing.module';
import { MatIconModule } from "@angular/material/icon";


@NgModule({
  declarations: [MuseumComponent],
  imports: [
    CommonModule,
    MuseumRoutingModule,
    MatIconModule
  ]
})
export class MuseumModule { }
