import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";

import { TeamRoutingModule } from "./team-routing.module";
import { TeamMainPageComponent } from "./team-main-page/team-main-page/team-main-page.component";
import { MatGridListModule } from "@angular/material/grid-list";
import { MatButtonModule } from "@angular/material/button";

@NgModule({
  declarations: [TeamMainPageComponent],
  imports: [
    CommonModule,
    TeamRoutingModule,
    MatGridListModule,
    MatButtonModule,
  ],
})
export class TeamModule {}
