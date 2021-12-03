import { Component } from "@angular/core";
import { FormControl, FormGroup, Validators } from "@angular/forms";
import {
  DeletingToolService,
  SelectionToolService,
} from "src/app/modules/workspace";

@Component({
  selector: "app-selection-tool-parameter",
  templateUrl: "./selection-tool-parameter.component.html",
  styleUrls: ["./selection-tool-parameter.component.scss"],
})
export class SelectionToolParameterComponent {
  form: FormGroup;
  private strokeWidth: FormControl;

  constructor(
    private selectionService: SelectionToolService,
    private deletingService: DeletingToolService
  ) {
    this.strokeWidth = new FormControl(1, [
      Validators.min(1),
      Validators.max(50),
    ]);
    this.form = new FormGroup({
      strokeWidth: this.strokeWidth,
    });
    this.selectionService.lineWidthSubject.subscribe((lineWidth: number) => {
      this.strokeWidth.setValue(lineWidth);
    });
  }

  get toolName(): string {
    return this.selectionService.toolName;
  }

  get hasSelection(): boolean {
    return this.selectionService.hasSelection();
  }

  /// SelectAll
  deleteSelection(): void {
    this.deletingService.deleteSelection();
  }

  changeLineWidth(): void {
    if (this.strokeWidth.value != null) {
      this.selectionService.setSelectionLineWidth(this.strokeWidth.value);
    }
  }
}
