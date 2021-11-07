import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class TeamClientService {
  private TEAM_ENDPOINT: string = environment.serverURL + "/teams";

  constructor(private httpClient: HttpClient) {}

  getTeams(): void {}
}
