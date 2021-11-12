import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { Avatar } from "src/app/shared/models/avatar.model";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class AvatarClientService {
  private AVATAR_ENDPOINT: string = environment.serverURL + "/avatars";

  constructor(private httpClient: HttpClient) {}

  getDefaultAvatars(): Observable<Avatar[]> {
    return this.httpClient.get<Avatar[]>(`${this.AVATAR_ENDPOINT}/default`);
  }

  uploadAvatar(avatar: FormData) {
    return this.httpClient
      .post(`${this.AVATAR_ENDPOINT}/upload`, avatar)
      .pipe((response) => {
        return response;
      });
  }
}
