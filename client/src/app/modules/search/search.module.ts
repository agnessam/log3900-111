import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";

import { SearchRoutingModule } from "./search-routing.module";
import { SearchResultsComponent } from "./search-results/search-results.component";
import { FlexModule } from "@angular/flex-layout";
import { UserCardComponent } from "./cards/user-card/user-card.component";
import { MatCardModule } from "@angular/material/card";
import { TeamCardComponent } from './cards/team-card/team-card.component';
import { DrawingCardComponent } from './cards/drawing-card/drawing-card.component';

@NgModule({
  declarations: [SearchResultsComponent, UserCardComponent, TeamCardComponent, DrawingCardComponent],
  imports: [CommonModule, SearchRoutingModule, FlexModule, MatCardModule],
})
export class SearchModule {}
