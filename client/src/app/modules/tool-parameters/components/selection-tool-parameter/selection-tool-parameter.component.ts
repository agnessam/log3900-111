import { Component } from '@angular/core';
import { CopyPasteToolService,
  DeletingToolService,
  SelectionToolService } from 'src/app/modules/workspace';

@Component({
  selector: 'app-selection-tool-parameter',
  templateUrl: './selection-tool-parameter.component.html',
  styleUrls: ['./selection-tool-parameter.component.scss'],
})
export class SelectionToolParameterComponent {

  constructor(
    private selectionService: SelectionToolService,
    private deletingService: DeletingToolService,
    private copyPasteService: CopyPasteToolService) { }

  get toolName(): string {
    return this.selectionService.toolName;
  }

  get hasClipboardObject(): boolean {
    return this.copyPasteService.hasClipboardObject();
  }

  get hasSelection(): boolean {
    return this.selectionService.hasSelection();
  }

  /// Copy
  copy(): void {
    this.copyPasteService.copy();
  }

  /// Cut
  cut(): void {
    this.copyPasteService.cut();
  }

  /// Paste
  paste(): void {
    this.copyPasteService.paste();
  }

  /// Duplicate
  duplicate(): void {
    this.copyPasteService.duplicate();
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
