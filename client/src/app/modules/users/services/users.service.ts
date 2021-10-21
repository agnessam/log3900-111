import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { EditableUserParameters } from "../models/editable-user-parameters";
import { User } from "../models/user";

@Injectable({
  providedIn: "root",
})
export class UsersService {
  private endpointUrl: string = environment.serverURL + "/users";
  private httpHeaders: HttpHeaders = new HttpHeaders().set(
    "ContentType",
    "application/x-www-form-urlencoded"
  );

  constructor(private httpClient: HttpClient) {}

  getUser(userId: string): Observable<User> {
    return this.httpClient
      .get(`${this.endpointUrl}/${userId}`)
      .pipe((response) => {
        return response;
      });
  }

  updateUser(userId: string, user: EditableUserParameters): Observable<User> {
    return this.httpClient
      .patch(`${this.endpointUrl}/${userId}`, user, {
        headers: this.httpHeaders,
      })
      .pipe((response) => {
        return response;
      });
  }
}
