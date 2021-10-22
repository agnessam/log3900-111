import { Injectable } from "@angular/core";
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
} from "@angular/common/http";
import { Observable } from "rxjs";
import { AuthenticationService } from "./services/authentication/authentication.service";

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
  jwtToken: string | null;

  constructor(private authenticationService: AuthenticationService) {
    this.authenticationService.authTokenObservable.subscribe(
      (token) => (this.jwtToken = token)
    );
  }

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    const re = "/login";
    if (request.url.search(re) === -1) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${localStorage.getItem("token")}`,
        },
      });
    }
    return next.handle(request);
  }
}
