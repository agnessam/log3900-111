import { Injectable } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { WorkspaceService } from "src/app/modules/workspace";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { map } from "rxjs/operators";
import { environment } from "src/environments/environment";

/// Service pour créer des nouveau canvas de dessin
@Injectable()
export class NewDrawingService {
  form: FormGroup;
  private isSizeModified = false;

  constructor(
    private formBuilder: FormBuilder,
    private workspaceService: WorkspaceService,
    private httpClient: HttpClient
  ) {
    this.form = this.formBuilder.group({
      size: this.formBuilder.group({
        width: this.formBuilder.control(0, [
          Validators.required,
          Validators.min(0),
          Validators.pattern("[0-9]*"),
        ]),
        height: this.formBuilder.control(0, [
          Validators.required,
          Validators.min(0),
          Validators.pattern("[0-9]*"),
        ]),
      }),
    });
    this.sizeGroup.valueChanges.subscribe((size) => {
      this.isSizeModified = !(
        size.width === this.workspaceService.width &&
        size.height === this.workspaceService.height
      );
      this.form.updateValueAndValidity();
    });
  }

  get sizeGroup(): FormGroup {
    return this.form.get("size") as FormGroup;
  }

  /// Réajuste le grandeur du workspace
  onResize(): void {
    if (!this.isSizeModified) {
      this.sizeGroup.setValue({
        width: this.workspaceService.width,
        height: this.workspaceService.height,
      });
    }
  }

  createNewDrawing(drawingDataUri: string ): Observable<any> {
    let newDrawing = {
      dataUri: drawingDataUri,
      ownerId: "617832e99a8c22d106b37528", // TODO: remove ownerID from all requests
      ownerModel: "User",
    };
    return this.httpClient
      .post<any>(`${environment.serverURL}/drawings/`, newDrawing)
      .pipe(
        map((response) => {
          return response;
        })
      );
  }
}
