import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, ReplaySubject, Subject } from "rxjs";
import { environment } from "src/environments/environment";
import { EditableChannelParameters } from "../models/editable-channel-parameters";
import { Message } from "../models/message.model";
import { TextChannel } from "../models/text-channel.model";

@Injectable({
  providedIn: "root",
})
export class TextChannelService {
  newTeamChannel: Subject<TextChannel>;
  deletedTeamChannel: Subject<string>;
  joinedCollabChannel: ReplaySubject<TextChannel>;
  leftCollabChannel: Subject<TextChannel>;
  closeChatEvent: Subject<boolean>;

  private endpointUrl: string = environment.serverURL + "/channels";
  private httpHeaders: HttpHeaders = new HttpHeaders().set(
    "ContentType",
    "application/x-www-form-urlencoded"
  );

  constructor(private httpClient: HttpClient) {
    this.newTeamChannel = new Subject<TextChannel>();
    this.deletedTeamChannel = new Subject<string>();
    this.joinedCollabChannel = new ReplaySubject<TextChannel>();
    this.leftCollabChannel = new Subject<TextChannel>();
    this.closeChatEvent = new Subject<boolean>();
  }

  getChannels(): Observable<TextChannel[]> {
    return this.httpClient
      .get<TextChannel[]>(this.endpointUrl)
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

  getTeamChannels(): Observable<TextChannel[]> {
    return this.httpClient
      .get<TextChannel[]>(`${this.endpointUrl}/teams`)
      .pipe((response) => {
        return response;
      });
  }

  getChannelByDrawingId(drawingId: string): Observable<TextChannel> {
    return this.httpClient
      .get<TextChannel>(`${this.endpointUrl}/drawings/${drawingId}`)
      .pipe((response) => {
        return response;
      });
  }

  createChannel(
    newName: string,
    newOwner: string,
    teamId?: string,
    drawingId?: string,
    isPrivate: boolean = false
  ): Observable<TextChannel> {
    return this.httpClient
      .post<TextChannel>(
        this.endpointUrl,
        {
          name: newName,
          ownerId: newOwner,
          team: teamId,
          drawing: drawingId,
          isPrivate: isPrivate,
        },
        {
          headers: this.httpHeaders,
        }
      )
      .pipe((response) => {
        return response;
      });
  }

  updateChannel(
    channelId: string,
    channel: EditableChannelParameters
  ): Observable<TextChannel> {
    return this.httpClient
      .patch<TextChannel>(`${this.endpointUrl}/${channelId}`, channel, {
        headers: this.httpHeaders,
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

  deleteMessages(channelId: string): Observable<void> {
    return this.httpClient
      .delete<void>(`${this.endpointUrl}/${channelId}/messages`)
      .pipe((response) => {
        return response;
      });
  }

  getMessages(channelId: string): Observable<Message[]> {
    return this.httpClient
      .get<Message[]>(`${this.endpointUrl}/${channelId}/messages`)
      .pipe((response) => {
        return response;
      });
  }

  searchChannels(query: string): Observable<TextChannel[]> {
    return this.httpClient.get<TextChannel[]>(
      `${this.endpointUrl}/all/search`,
      {
        params: new HttpParams().set("q", query),
      }
    );
  }

  emitNewTeamChannel(channel: TextChannel): void {
    this.newTeamChannel.next(channel);
  }

  emitDeleteTeamChannel(channelName: string): void {
    this.deletedTeamChannel.next(channelName);
  }

  emitJoinCollaboration(channel: TextChannel): void {
    this.joinedCollabChannel.next(channel);
  }

  emitLeaveCollaboration(channel: TextChannel): void {
    this.leftCollabChannel.next(channel);
  }

  emitCloseChat(): void {
    this.closeChatEvent.next(true);
  }
}
