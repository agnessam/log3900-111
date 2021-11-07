import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";
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

  createTeam(
    teamName: string,
    description: string,
    ownerId: string = "617832e99a8c22d106b37528"
  ): Observable<Team> {
    let newTeam = {
      name: teamName,
      description: description,
      ownerId: ownerId,
    };

    return this.httpClient.post<Team>(`${this.TEAM_ENDPOINT}`, newTeam).pipe(
      map((team) => {
        return team;
      })
    );
  }
}
