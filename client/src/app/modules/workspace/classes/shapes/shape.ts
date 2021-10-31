import { RGB } from "src/app/shared";

export abstract class Shape {
  name: string;
  pathData: number[];
  fillStyle: string;
  color: RGB;
}
