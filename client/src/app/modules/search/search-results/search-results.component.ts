import { ChangeDetectorRef, Component, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { Drawing } from "src/app/shared";
import { SearchResult } from "src/app/shared/models/search-result.model";
import { Team } from "src/app/shared/models/team.model";
import { SearchClientService } from "../../backend-communication/search-client/search-client.service";
import { User } from "../../users/models/user";

@Component({
  selector: "app-search-results",
  templateUrl: "./search-results.component.html",
  styleUrls: ["./search-results.component.scss"],
})
export class SearchResultsComponent implements OnInit {
  userId: string;

  searchQuery: string;
  searchResults: SearchResult;
  filteredDrawings: Drawing[] = [];

  searchCompleted: Promise<boolean>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private searchClient: SearchClientService,
    private changeDetector: ChangeDetectorRef
  ) {
    this.userId = localStorage.getItem("userId")!;
    this.activatedRoute.queryParams.subscribe((params) => {
      this.searchQuery = params.q;
      this.filteredDrawings = [];
      this.searchClient.search(this.searchQuery).subscribe((searchResult) => {
        this.searchResults = searchResult;
        this.filterPrivateDrawings(this.searchResults.drawings);

        this.searchCompleted = Promise.resolve(true);
      });
    });
  }

  ngOnInit(): void {}

  deleteDrawingFromView(deletedDrawing: Drawing) {
    this.searchResults.drawings.splice(
      this.searchResults.drawings.indexOf(deletedDrawing),
      1
    );
    this.changeDetector.detectChanges();
  }

  filterPrivateDrawings(drawings: Drawing[]) {
    for (let drawing of drawings) {
      if (this.hasPermission(drawing)) {
        this.filteredDrawings.push(drawing);
      }
    }
  }

  hasPermission(drawing: Drawing): boolean {
    if (drawing.privacyLevel != "private") return true;

    if (
      drawing.ownerModel == "User" &&
      (drawing.owner as User)._id == this.userId
    ) {
      return true;
    }

    if (drawing.ownerModel == "Team") {
      if (((drawing.owner as Team).members as string[]).includes(this.userId)) {
        return true;
      }
    }

    return false;
  }
}
