import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from "src/environments/environment";
import { Observable } from 'rxjs';
import { map } from "rxjs/operators";
import { Drawing } from "src/app/shared/models/drawing.model";

@Injectable({
  providedIn: 'root'
})
export class DrawingHttpClientService {
  private SERVER_ENDPOINT:string = environment.serverURL + "/drawings/";
  constructor(private httpClient: HttpClient) { }

  getDrawings(): Observable<Drawing[]> {
    return this.httpClient.get<Drawing[]>(this.SERVER_ENDPOINT);
  }

  getDrawing(drawingId:string): Observable<Drawing>{
    return this.httpClient.get<Drawing>(`${this.SERVER_ENDPOINT}${drawingId}`);
  }

  createNewDrawing(drawingDataUri: string, ownerModel:string, ownerId: string = "617832e99a8c22d106b37528"): Observable<Drawing> {
    let newDrawing = {
      dataUri: drawingDataUri,
      ownerId: ownerId, // TODO: remove ownerID from all requests
      ownerModel: ownerModel, // TODO: Add option to toggle between 
    };
    return this.httpClient
      .post<Drawing>(`${this.SERVER_ENDPOINT}`, newDrawing)
      .pipe(
        map((response) => {
          return response;
        })
      );
  }
}
