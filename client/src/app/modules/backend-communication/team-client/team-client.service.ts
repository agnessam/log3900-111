import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Team } from "src/app/shared/models/team.model";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class TeamClientService {
  private TEAM_ENDPOINT: string = environment.serverURL + "/teams/";

  constructor(private httpClient: HttpClient) {}

  getTeams(): Observable<Team[]> {
    return this.httpClient.get<Team[]>(this.TEAM_ENDPOINT);
  }

  getTeam(teamId: string): Observable<Team> {
    return this.httpClient.get<Team>(`${this.TEAM_ENDPOINT}${teamId}`);
  }
}
