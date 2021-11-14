import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class SearchClientService {
  private SEARCH_ENDPOINT: string = environment.serverURL + "/search";

  constructor(private httpClient: HttpClient) {}

  search(query: string) {
    return this.httpClient.get(`${this.SEARCH_ENDPOINT}`, {
      params: new HttpParams().set("q", query),
    });
  }
}
