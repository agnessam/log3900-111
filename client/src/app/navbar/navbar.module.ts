import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { NavbarComponent } from "./navbar.component";
import { SharedModule } from "../shared/shared.module";
import { RouterModule } from '@angular/router';

@NgModule({
  exports: [NavbarComponent],
  declarations: [NavbarComponent],
  imports: [CommonModule, SharedModule, RouterModule],
})
export class NavbarModule {}
