import { Component, HostListener, OnInit } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { MatDialogRef } from "@angular/material/dialog";
import { MatSnackBar } from "@angular/material/snack-bar";
import { ColorPickerService } from "src/app/modules/color-picker";
import { DEFAULT_RGB_COLOR, DEFAULT_ALPHA } from "src/app/shared";
import { DrawingService } from "src/app/modules/workspace";
import { NewDrawingService } from "./new-drawing.service";
import { GridService } from "src/app/modules/workspace";
// import { NewDrawingAlertComponent } from "./new-drawing-alert/new-drawing-alert.component";
import { Router } from "@angular/router";

const ONE_SECOND = 1000;
@Component({
  selector: "app-new-drawing",
  templateUrl: "./new-drawing.component.html",
  styleUrls: ["./new-drawing.component.scss"],
})
export class NewDrawingComponent implements OnInit {
  form: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<NewDrawingComponent>,
    private snackBar: MatSnackBar,
    private newDrawingService: NewDrawingService,
    private drawingService: DrawingService,
    private colorPickerService: ColorPickerService,
    private gridService: GridService,
    private router: Router
  ) {}

  /// Créer un nouveau form avec les dimensions et la couleur
  ngOnInit(): void {
    this.form = new FormGroup({
      dimension: this.newDrawingService.form,
      color: this.colorPickerService.colorForm,
    });
    this.dialogRef.disableClose = true;
    this.dialogRef.afterOpened().subscribe(() => this.onResize());
    this.colorPickerService.setFormColor(DEFAULT_RGB_COLOR, DEFAULT_ALPHA);
  }

  get sizeForm(): FormGroup {
    return (this.form.get("dimension") as FormGroup).get("size") as FormGroup;
  }

  /// Ouvre le dialog pour l'alerte lorsque le service est creer
  onAccept(): void {
    this.newDrawingService.createNewDrawing().subscribe((response) => {
      if (response._id) {
        this.router.navigate([`/drawings/${response._id}`]).then(() => {
          setTimeout(() => {
            this.newDrawing();
          }, 200);
        });
      }
    });
  }

  /// Cree un nouveau dessin
  private newDrawing() {
    this.gridService.activateGrid.setValue(false);
    this.drawingService.isCreated = true;
    const size: { width: number; height: number } =
      this.newDrawingService.sizeGroup.value;
    this.drawingService.newDrawing(size.width, size.height, {
      rgb: this.colorPickerService.rgb.value,
      a: this.colorPickerService.a.value,
    });
    this.snackBar.open("Nouveau dessin créé", "", { duration: ONE_SECOND });
    this.newDrawingService.form.reset();
    this.dialogRef.close();
  }

  /// Ferme le dialogue
  onCancel(): void {
    this.dialogRef.close();
  }

  /// Ecoute pour un changement de la grandeur du window
  @HostListener("window:resize", ["$event"])
  onResize() {
    this.newDrawingService.onResize();
  }
}
