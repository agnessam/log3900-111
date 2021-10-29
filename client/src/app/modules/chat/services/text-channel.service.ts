import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { EditableChannelParameters } from '../models/editable-channel-parameters';
import { TextChannel } from '../models/text-channel.model';

@Injectable({
  providedIn: 'root'
})
export class TextChannelService {

  private endpointUrl: string = environment.serverURL + "/channels/";
  private httpHeaders: HttpHeaders = new HttpHeaders().set(
    "ContentType",
    "application/x-www-form-urlencoded",
  );

  constructor(private httpClient: HttpClient) { }

  getChannels(): Observable<TextChannel> {
    return this.httpClient
      .get<TextChannel>(this.endpointUrl)
      .pipe((response) => {
        return response;
      });
  }

  getChannel(channelId: string): Observable<TextChannel> {
    return this.httpClient
      .get<TextChannel>(`${this.endpointUrl}/${channelId}`)
      .pipe((response) => {
        return response;
      });
  }

  updateChannel(channelId: string, channel: EditableChannelParameters): Observable<TextChannel> {
    return this.httpClient
      .patch<TextChannel>(`${this.endpointUrl}/${channelId}`, channel, {
        headers: this.httpHeaders
      })
      .pipe((response) => {
        return response;
      });
  }

  deleteChannel(channelId: string): Observable<TextChannel> {
    return this.httpClient
      .delete<TextChannel>(`${this.endpointUrl}/${channelId}`)
      .pipe((response) => {
        return response;
      });
  }
}
