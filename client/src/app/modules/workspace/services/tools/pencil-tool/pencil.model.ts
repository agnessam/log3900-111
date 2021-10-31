import { Point } from "src/app/shared";

export interface Pencil {
  name: string;
  pointsList: Point[];
  strokeWidth: number;
  fill: string;
  stroke: string;
  fillOpacity: string;
  strokeOpacity: string;
}
