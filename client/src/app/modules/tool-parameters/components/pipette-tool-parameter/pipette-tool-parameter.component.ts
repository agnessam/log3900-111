import { Component } from '@angular/core';
import { PipetteToolService } from 'src/app/modules/workspace';

@Component({
  selector: 'app-pipette-tool-parameter',
  templateUrl: './pipette-tool-parameter.component.html',
  styleUrls: ['./pipette-tool-parameter.component.scss'],
})

/// Le component d'affichage des paramètres de la pipette de couleur
export class PipetteToolParameterComponent {

  constructor(private pipetteToolService: PipetteToolService) { }

  get toolName(): string {
    return this.pipetteToolService.toolName;
  }
}
