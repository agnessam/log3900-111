import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { environment } from "src/environments/environment";
import { EditableUserParameters } from "../models/editable-user-parameters";

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

  updateUser(userId: string, user: EditableUserParameters) {
    return this.httpClient
      .patch(`${this.endpointUrl}/${userId}`, user, {
        headers: this.httpHeaders,
      })
      .pipe((response) => {
        return response;
      });
  }
}
