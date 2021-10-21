import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { environment } from "src/environments/environment";
import { AuthenticationService } from "../../authentication";
import { User } from "../../authentication/models/user";
import { EditableUserParameters } from "../models/user";

@Injectable({
  providedIn: "root",
})
export class UsersService {
  private endpointUrl: string = environment.serverURL + "/users";
  private currentUser: User | null;

  constructor(
    private httpClient: HttpClient,
    private authenticationService: AuthenticationService
  ) {
    this.authenticationService.currentUserObservable.subscribe((user) => {
      console.log(user);
      this.currentUser = user;
    });
  }

  updateUser(user: EditableUserParameters) {
    const httpOptions = {
      params: new HttpParams().set("id", this.currentUser!._id),
    };
    console.log(this.currentUser);
    console.log(this.currentUser!._id);
    this.httpClient.patch(this.endpointUrl, user, httpOptions);
  }
}
