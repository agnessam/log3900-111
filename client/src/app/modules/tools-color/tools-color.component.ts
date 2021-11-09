import { Component } from "@angular/core";
import { MatDialog, MatDialogRef } from "@angular/material/dialog";
import { Subscription } from "rxjs";
import { RGB, RGBA } from "src/app/shared";
import { ToolsColorService } from "./services/tools-color.service";
import { ToolsColorPickerComponent } from "./tools-color-picker/tools-color-picker.component";
import {
  TOOLS_COLOR_PICKER_LEFT,
  TOOLS_COLOR_PICKER_TOP,
  TOOLS_COLOR_PICKER_WIDTH,
} from "./tools-color-picker/tools-color-picker.constant";
import {
  ColorType,
  PRIMARY_AND_SECONDARY_SIZE,
  PRIMARY_SIZE,
  SECONDARY_SIZE,
} from "./tools-color.constant";

@Component({
  selector: "app-tools-color",
  templateUrl: "./tools-color.component.html",
  styleUrls: ["./tools-color.component.scss"],
})
export class ToolsColorComponent {
  width = PRIMARY_AND_SECONDARY_SIZE.width;
  height = PRIMARY_AND_SECONDARY_SIZE.height;

  dialogSub: Subscription;
  dialogRef: MatDialogRef<ToolsColorPickerComponent>;

  readonly primarySize: {
    x: number;
    y: number;
    width: number;
    height: number;
  } = PRIMARY_SIZE;
  readonly secondarySize: {
    x: number;
    y: number;
    width: number;
    height: number;
  } = SECONDARY_SIZE;

  constructor(
    private toolsColor: ToolsColorService,
    public dialog: MatDialog
  ) {}

  get primaryColor(): string {
    return this.toolsColor.primaryColorString;
  }

  get primaryAlpha(): number {
    return this.toolsColor.primaryAlpha;
  }

  get secondaryColor(): string {
    return this.toolsColor.secondaryColorString;
  }

  get secondaryAlpha(): number {
    return this.toolsColor.secondaryAlpha;
  }

  /// Ouvre un dialog qui fait appel a colorPickerOpen
  openDialog(colorType: ColorType): void {
    switch (colorType) {
      case ColorType.primary:
        this.dialogRef = this.colorPickerOpen(
          this.toolsColor.primaryColor,
          this.toolsColor.primaryAlpha
        );
        this.dialogSub = this.dialogRef
          .afterClosed()
          .subscribe((result: RGBA) => {
            if (result) {
              this.toolsColor.setPrimaryColor(result.rgb, result.a);
            }
          });
        break;
      case ColorType.secondary:
        this.dialogRef = this.colorPickerOpen(
          this.toolsColor.secondaryColor,
          this.toolsColor.secondaryAlpha
        );
        this.dialogSub = this.dialogRef
          .afterClosed()
          .subscribe((result: RGBA) => {
            if (result) {
              this.toolsColor.setSecondaryColor(result.rgb, result.a);
            }
          });
        break;
      default:
        break;
    }
  }

  /// Ouvre le dialog pour le choix de couleur
  private colorPickerOpen(
    rgb: RGB,
    a: number
  ): MatDialogRef<ToolsColorPickerComponent> {
    let dialogRef: MatDialogRef<ToolsColorPickerComponent>;
    dialogRef = this.dialog.open(ToolsColorPickerComponent, {
      width: TOOLS_COLOR_PICKER_WIDTH,
      data: { rgb, a },
    });
    dialogRef.updatePosition({
      top: TOOLS_COLOR_PICKER_TOP,
      left: TOOLS_COLOR_PICKER_LEFT,
    });
    return dialogRef;
  }

  /// Échange les couleurs entre la principale et secondaire
  switchColor(): void {
    this.toolsColor.switchColor();
  }

  /// click de souris ouvre le dialog pour la couleur primaire
  clickPrimary(event: MouseEvent): void {
    this.openDialog(ColorType.primary);
  }
  /// click de souris ouvre le dialog pour la couleur secondaire
  clickSecondary(event: MouseEvent): void {
    this.openDialog(ColorType.secondary);
  }
}
