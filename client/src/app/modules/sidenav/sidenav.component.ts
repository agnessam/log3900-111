import { Component } from "@angular/core";
import { MatButtonToggleChange } from "@angular/material/button-toggle";
import { SidenavService } from "./services/sidenav/sidenav.service";
import { Tools, ToolsService } from "src/app/modules/workspace";
import { PostService } from "../museum/services/post.service";

@Component({
  selector: "app-sidenav",
  templateUrl: "./sidenav.component.html",
  styleUrls: ["./sidenav.component.scss"],
})
export class SidenavComponent {
  constructor(
    private sideNavService: SidenavService,
    private toolService: ToolsService,
    private postService: PostService,
  ) {}

  get currentToolId(): number {
    return this.toolService.selectedToolId;
  }

  get toolList(): Map<number, Tools> {
    return this.sideNavService.toolList;
  }

  get isOpened(): boolean {
    return this.sideNavService.isOpened;
  }

  /// Choisit un parametre
  get selectedParameter(): number {
    return this.sideNavService.selectedParameter;
  }

  /// Ouvre le sidenav
  open(): void {
    this.sideNavService.open();
  }

  /// Ferme le sidenav
  close(): void {
    this.sideNavService.close();
  }

  /// Changer la selection avec un toggle button
  selectionChanged(selectedItem: MatButtonToggleChange): void {
    this.sideNavService.selectionChanged(selectedItem);
  }

  /// Ouvre le menu control
  openControlMenu(): void {
    this.sideNavService.openControlMenu();
  }

  publishMuseum(): void {
    this.postService.postCanvas();
  }
}
