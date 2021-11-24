import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { environment } from "src/environments/environment";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";
import { Drawing } from "src/app/shared/";

@Injectable({
  providedIn: "root",
})
export class DrawingHttpClientService {
  private SERVER_ENDPOINT: string = environment.serverURL + "/drawings/";
  constructor(private httpClient: HttpClient) {}

  getDrawings(): Observable<Drawing[]> {
    return this.httpClient.get<Drawing[]>(this.SERVER_ENDPOINT);
  }

  getDrawing(drawingId: string): Observable<Drawing> {
    return this.httpClient.get<Drawing>(`${this.SERVER_ENDPOINT}${drawingId}`);
  }

  updateDrawing(
    drawingId: string,
    drawingDataUri: string
  ): Observable<Drawing> {
    let newDrawing = {
      dataUri: drawingDataUri,
    };
    return this.httpClient.patch<Drawing>(
      `${this.SERVER_ENDPOINT}${drawingId}`,
      newDrawing
    );
  }

  createNewDrawing(
    drawingDataUri: string,
    ownerModel: string,
    ownerId: string,
    name: string
  ): Observable<Drawing> {
    let newDrawing = {
      dataUri: drawingDataUri,
      owner: ownerId,
      ownerModel: ownerModel,
      name: name,
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
