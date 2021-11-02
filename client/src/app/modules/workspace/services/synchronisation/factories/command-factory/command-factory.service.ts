import { Injectable } from '@angular/core';
import { DrawingService } from '../../../drawing/drawing.service';
import { ICommand } from 'src/app/modules/workspace/interfaces/command.interface';
import { PencilCommand, RendererProviderService } from 'src/app/modules/workspace';

@Injectable({
  providedIn: 'root'
})
export class CommandFactoryService {

  constructor(private drawingService: DrawingService, private rendererService: RendererProviderService) { }

  createCommand(commandType: string, commandParameters: any): ICommand | null {
    switch(commandType) {
      case 'Pencil':
        return new PencilCommand(this.rendererService.renderer, commandParameters, this.drawingService);
      default: 
        return null;
    }
  }
}
