import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BehaviorSubject, Observable } from "rxjs";
import { map } from "rxjs/operators";
import { environment } from "src/environments/environment";
import { User } from "../../models/user";

@Injectable({
  providedIn: "root",
})
export class AuthenticationService {
  private endpointUrl: string = environment.serverURL + "/auth/";

  private authTokenSubject: BehaviorSubject<string | null>;
  public authTokenObservable: Observable<string | null>;

  private currentUserSubject: BehaviorSubject<User | null>;
  public currentUserObservable: Observable<User | null>;

  constructor(private httpClient: HttpClient) {
    this.authTokenSubject = new BehaviorSubject<string | null>(null);
    this.currentUserSubject = new BehaviorSubject<User | null>(null);

    this.authTokenObservable = this.authTokenSubject.asObservable();
    this.currentUserObservable = this.currentUserSubject.asObservable();
  }

  public get authToken(): string | null {
    return this.authTokenSubject.value;
  }

  public get username(): string | null {
    return this.currentUserSubject.value!.username;
  }

  public isAuthenticated(): boolean {
    // Covers the case where the user deletes the token himself.
    if (!localStorage.getItem("token")) {
      this.authTokenSubject.next(null);
      this.currentUserSubject.next(null);
      return false;
    }

    if (this.authToken == null) {
      return false;
    }

    return true;
  }

  public login(username: string, password: string) {
    const loginEndpoint = this.endpointUrl + "login";
    return this.httpClient
      .post<any>(loginEndpoint, { username: username, password: password })
      .pipe(
        map((response) => {
          if (response.error) {
            return response;
          }

          localStorage.setItem("token", response.token);
          localStorage.setItem("userId", response.user._id);

          this.authTokenSubject.next(response.token);
          this.currentUserSubject.next(response.user);

          return response;
        })
      );
  }

  public register(
    username: string,
    password: string,
    email: string,
    firstName: string,
    lastName: string
  ) {
    const registerEndpoint = this.endpointUrl + "register";
    return this.httpClient
      .post<any>(registerEndpoint, {
        username: username,
        password: password,
        email: email,
        firstName: firstName,
        lastName: lastName,
      })
      .pipe(
        map((response) => {
          if (response.error) {
            return response;
          }

          localStorage.setItem("token", response.token);
          localStorage.setItem("userId", response.user._id);

          this.authTokenSubject.next(response.token);
          this.currentUserSubject.next(response.user);
          return response;
        })
      );
  }

  public logout() {
    const logoutEndpoint = this.endpointUrl + "logout";
    return this.httpClient.post<any>(logoutEndpoint, {}).pipe(
      map((response) => {
        localStorage.removeItem("userId");
        localStorage.removeItem("token");

        this.authTokenSubject.next(null);
        this.currentUserSubject.next(null);
        return response;
      })
    );
  }
}
