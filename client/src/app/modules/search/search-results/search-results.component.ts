import { Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { SearchClientService } from "../../backend-communication/search-client/search-client.service";

@Component({
  selector: "app-search-results",
  templateUrl: "./search-results.component.html",
  styleUrls: ["./search-results.component.scss"],
})
export class SearchResultsComponent implements OnInit {
  constructor(
    private activatedRoute: ActivatedRoute,
    private searchClient: SearchClientService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe((params) => {
      const searchQuery: string = params.q;
      this.searchClient.search(searchQuery).subscribe((searchResult) => {
        console.log(searchResult);
      });
    });
  }
}
