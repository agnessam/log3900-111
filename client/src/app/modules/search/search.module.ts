import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";

import { SearchRoutingModule } from "./search-routing.module";
import { SearchResultsComponent } from "./search-results/search-results.component";
import { FlexModule } from "@angular/flex-layout";

@NgModule({
  declarations: [SearchResultsComponent],
  imports: [CommonModule, SearchRoutingModule, FlexModule],
})
export class SearchModule {}
