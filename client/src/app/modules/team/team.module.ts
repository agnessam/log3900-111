import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";

import { TeamRoutingModule } from "./team-routing.module";
import { TeamMainPageComponent } from "./team-main-page/team-main-page/team-main-page.component";
import { MatGridListModule } from "@angular/material/grid-list";
import { MatButtonModule } from "@angular/material/button";
import { MatCardModule } from "@angular/material/card";
import { FlexLayoutModule } from "@angular/flex-layout";
import { TeamProfileComponent } from './team-profile/team-profile/team-profile.component';
import { NewTeamComponent } from './new-team/new-team/new-team.component';

@NgModule({
  declarations: [TeamMainPageComponent, TeamProfileComponent, NewTeamComponent],
  imports: [
    CommonModule,
    TeamRoutingModule,
    MatGridListModule,
    MatButtonModule,
    MatCardModule,
    FlexLayoutModule,
  ],
})
export class TeamModule {}
