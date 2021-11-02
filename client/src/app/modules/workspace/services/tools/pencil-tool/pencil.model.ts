import { Point } from "src/app/shared";

export interface Pencil {
  id: string;
  pointsList: Point[];
  strokeWidth: number;
  fill: string;
  stroke: string;
  fillOpacity: string;
  strokeOpacity: string;
}

export interface InProgressPencil {
  id: string;
  point: Point;
}
