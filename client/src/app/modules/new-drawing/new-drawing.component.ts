import { Component, HostListener, OnInit } from "@angular/core";
import { FormControl, FormGroup } from "@angular/forms";
import { MatDialogRef } from "@angular/material/dialog";
import { MatSnackBar } from "@angular/material/snack-bar";
import { Router } from "@angular/router";
import { ColorPickerService } from "src/app/modules/color-picker";
import { DrawingService } from "src/app/modules/workspace";
import { DEFAULT_ALPHA, DEFAULT_RGB_COLOR } from "src/app/shared";
import { DrawingHttpClientService } from "../backend-communication";
import { NewDrawingService } from "./new-drawing.service";

const ONE_SECOND = 1000;
@Component({
  selector: "app-new-drawing",
  templateUrl: "./new-drawing.component.html",
  styleUrls: ["./new-drawing.component.scss"],
})
export class NewDrawingComponent implements OnInit {
  form: FormGroup;
  teams:String[] = [];

  constructor(
    public dialogRef: MatDialogRef<NewDrawingComponent>,
    private snackBar: MatSnackBar,
    private newDrawingService: NewDrawingService,
    private drawingService: DrawingService,
    private colorPickerService: ColorPickerService,
    private drawingHttpClient: DrawingHttpClientService,
    private router: Router
  ) {}

  /// Créer un nouveau form avec les dimensions et la couleur
  ngOnInit(): void {
    this.form = new FormGroup({
      name: new FormControl(""),
      teamName: new FormControl(""),
      ownerId: new FormControl(""),
      dimension: this.newDrawingService.form,
      color: this.colorPickerService.colorForm,
    });
    this.dialogRef.disableClose = true;
    this.dialogRef.afterOpened().subscribe(() => this.onResize());
    this.colorPickerService.setFormColor(DEFAULT_RGB_COLOR, DEFAULT_ALPHA);
    // fetch all teams
  }

  get sizeForm(): FormGroup {
    return (this.form.get("dimension") as FormGroup).get("size") as FormGroup;
  }

  /// Ouvre le dialog pour l'alerte lorsque le service est creer
  onAccept(): void {
    this.drawingService.isCreated = true;
    const size: { width: number; height: number } =
      this.newDrawingService.sizeGroup.value;
    let drawingDataUri = this.drawingService.newDrawing(
      size.width,
      size.height,
      {
        rgb: this.colorPickerService.rgb.value,
        a: this.colorPickerService.a.value,
      }
    );
    let ownerModel: string = this.form.value.ownerId == "" ? "User": "Team";
    let ownerId: string = this.form.value.ownerId;
    let drawingName: string = this.form.value.name;
    this.drawingHttpClient
      .createNewDrawing(drawingDataUri, ownerModel, ownerId, drawingName)
      .subscribe((response) => {
        if (response._id) {
          this.router.navigate([`/drawings/${response._id}`]);
          this.snackBar.open("Nouveau dessin créé", "", {
            duration: ONE_SECOND,
          });
        }
      });
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
