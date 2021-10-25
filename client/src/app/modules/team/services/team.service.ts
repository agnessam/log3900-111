import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { EditableTeamParameters } from "../models/editable-team-parameters";
import { Team } from "../models/team.model";

@Injectable({
  providedIn: "root",
})
export class TeamService {
  private endpointUrl: string = environment.serverURL + "/teams";
  private httpHeaders: HttpHeaders = new HttpHeaders().set(
    "ContentType",
    "application/x-www-form-urlencoded"
  );

  constructor(private httpClient: HttpClient) {}

  getTeams(): Observable<Team[]> {
    return this.httpClient
      .get<Team[]>(`${this.endpointUrl}`)
      .pipe((response) => {
        return response;
      });
  }

  getTeam(teamId: string): Observable<Team> {
    return this.httpClient
      .get(`${this.endpointUrl}/${teamId}`)
      .pipe((response) => {
        return response;
      });
  }

  updateTeam(teamId: string, team: EditableTeamParameters): Observable<Team> {
    return this.httpClient
      .patch(`${this.endpointUrl}/${teamId}`, team, {
        headers: this.httpHeaders,
      })
      .pipe((response) => {
        return response;
      });
  }

  createTeam(team: Team): Observable<Team> {
    return this.httpClient
      .post(`${this.endpointUrl}`, team, {
        headers: this.httpHeaders,
      })
      .pipe((response) => {
        return response;
      });
  }
}
