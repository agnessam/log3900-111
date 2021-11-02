import { Injectable } from "@angular/core";
import { DrawingService } from "../..";

@Injectable({
  providedIn: "root",
})
export class SynchronisationService {
  constructor(private drawingService: DrawingService) {}
}
