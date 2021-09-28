import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { map } from "rxjs/operators";

@Injectable({
  providedIn: "root",
})
export class AuthenticationService {
  private endpointUrl: string = "http://localhost:3000/api/v1/";
  private authTokenSubject: BehaviorSubject<string | null>;
  public authTokenObservable: Observable<string | null>;

  constructor(private httpClient: HttpClient) {
    const userToken = localStorage.getItem("user_token");
    if (!userToken) {
      this.authTokenSubject = new BehaviorSubject<string | null>(null);
    } else {
      this.authTokenSubject = new BehaviorSubject<string | null>(
        JSON.parse(userToken)
      );
    }

    this.authTokenObservable = this.authTokenSubject.asObservable();
  }

  public get authToken(): string | null {
    return this.authTokenSubject.value;
  }

  public isAuthenticated(): boolean {
    if (this.authToken == null) {
      return false;
    }

    return true;
  }

  login(username: string, password: string) {
    const loginEndpoint = this.endpointUrl + "login";
    return this.httpClient
      .post<any>(loginEndpoint, { username: username, password: password })
      .pipe(
        map((user) => {
          if (!user.token) {
            return user;
          }
          localStorage.setItem("user_token", JSON.stringify(user.token));
          this.authTokenSubject.next(user.token);
          return user;
        })
      );
  }
}
