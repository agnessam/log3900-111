import { Component, Input } from "@angular/core";
import { MatButtonToggleChange } from "@angular/material/button-toggle";
import { SidenavService } from "./services/sidenav/sidenav.service";
import { Tools, ToolsService } from "src/app/modules/workspace";
import { MatDialog } from "@angular/material/dialog";
import { MuseumDialog } from "../museum/museum-dialog/museum-dialog.component";
import { Drawing } from "src/app/shared";
import { DrawingHttpClientService } from "../backend-communication";
import { User } from "../users/models/user";
import { Team } from "src/app/shared/models/team.model";

@Component({
  selector: "app-sidenav",
  templateUrl: "./sidenav.component.html",
  styleUrls: ["./sidenav.component.scss"],
})
export class SidenavComponent {
  @Input() drawingId: string;
  drawing: Drawing;

  drawingLoaded: Promise<boolean>;

  constructor(
    private sideNavService: SidenavService,
    private toolService: ToolsService,
    private drawingClient: DrawingHttpClientService,
    public dialog: MatDialog
  ) {}

  ngOnInit() {
    this.drawingClient.getDrawing(this.drawingId).subscribe((drawing) => {
      this.drawing = drawing;
      this.drawingLoaded = Promise.resolve(true);
    });
  }

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

  openMuseumDialog(): void {
    this.dialog.open(MuseumDialog, { data: this.drawingId });
  }

  isOwner(): boolean {
    const userId = localStorage.getItem("userId")!;
    if (this.drawing.ownerModel == "User") {
      return (this.drawing.owner as User)._id == userId;
    }

    return ((this.drawing.owner as Team).members as string[]).includes(userId);
  }
}
