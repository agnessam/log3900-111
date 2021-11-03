import { Component } from "@angular/core";
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
  constructor(
    private selectionService: SelectionToolService,
    private deletingService: DeletingToolService
  ) {}

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

  /// SelectAll
  selectAll(): void {
    this.selectionService.selectAll();
  }
}
