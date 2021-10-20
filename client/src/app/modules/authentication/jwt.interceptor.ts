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
  constructor(private authenticationService: AuthenticationService) {}

  intercept(
    request: HttpRequest<unknown>,
    next: HttpHandler
  ): Observable<HttpEvent<unknown>> {
    const re = "/login";
    if (request.url.search(re) === -1) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${this.authenticationService.authToken}`,
        },
      });
    }
    return next.handle(request);
  }
}
