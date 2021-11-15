import { Drawing } from "..";

export interface Team {
  _id: string;

  name: string;
  description: string;

  owner: string;

  members: string[];

  drawings: string[] | Drawing[];
}
