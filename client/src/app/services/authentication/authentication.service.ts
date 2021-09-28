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

  // TODO: Seperate into different class
  private currentUserSubject: BehaviorSubject<string | null>;
  public currentUserObservable: Observable<string | null>;

  constructor(private httpClient: HttpClient) {
    const userToken = localStorage.getItem("user_token");
    if (!userToken) {
      this.authTokenSubject = new BehaviorSubject<string | null>(null);
      this.currentUserSubject = new BehaviorSubject<string | null>(null);
    } else {
      this.authTokenSubject = new BehaviorSubject<string | null>(
        JSON.parse(userToken)
      );
      this.currentUserSubject = new BehaviorSubject<string | null>(
        JSON.parse(localStorage.getItem("username")!)
      );
    }

    this.authTokenObservable = this.authTokenSubject.asObservable();
    this.currentUserObservable = this.currentUserSubject.asObservable();
  }

  public get authToken(): string | null {
    return this.authTokenSubject.value;
  }

  public get username(): string | null {
    return this.currentUserSubject.value;
  }

  public isAuthenticated(): boolean {
    // Covers the case where the user deletes the token himself.
    if (!localStorage.getItem("user_token")) {
      this.authTokenSubject.next(null);
      this.currentUserSubject.next(null);
      return false;
    }

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
          localStorage.setItem("username", JSON.stringify(user.username));

          this.authTokenSubject.next(user.token);
          this.currentUserSubject.next(user.username);
          return user;
        })
      );
  }

  logout(username: string | null) {
    const logoutEndpont = this.endpointUrl + "logout";
    return this.httpClient
      .post<any>(logoutEndpont, { username: username })
      .pipe(
        map((response) => {
          console.log(response);
          localStorage.removeItem("username");
          localStorage.removeItem("user_token");

          this.authTokenSubject.next(null);
          this.currentUserSubject.next(null);
          return response;
        })
      );
  }
}
