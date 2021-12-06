import { Request } from 'express';
import { inject } from 'inversify';
import {
  controller,
  httpDelete,
  httpGet,
  httpPatch,
  httpPost,
  request,
} from 'inversify-express-utils';
import { TYPES } from '../../domain/constants/types';
import { DrawingRepository } from '../../infrastructure/data_access/repositories/drawing_repository';
import passport from 'passport';
import { CollaborationTrackerService } from '../../domain/services/collaboration-tracker.service';

@controller('/drawings', passport.authenticate('jwt', { session: false }))
export class DrawingController {
  @inject(TYPES.DrawingRepository) public drawingRepository: DrawingRepository;
  @inject(TYPES.CollaborationTrackerService)
  public collaborationTrackerService: CollaborationTrackerService;

  @httpGet('/')
  public async get() {
    return await this.drawingRepository.getPopulatedDrawings();
  }

  @httpGet('/collaborators')
  public getDrawingCollaborators() {
    return this.collaborationTrackerService.getDrawingCollaborators();
  }

  @httpGet('/:drawingId')
  public async getDrawingById(@request() req: Request) {
    return await this.drawingRepository.getPopulatedDrawing(
      req.params.drawingId,
    );
  }

  @httpPost('/')
  public async createDrawing(@request() req: Request) {
    return req.body.ownerModel === 'User'
      ? await this.drawingRepository.createUserDrawing(req.body, req.user!.id)
      : await this.drawingRepository.createTeamDrawing(req.body);
  }

  @httpPatch('/:drawingId')
  public async updateDrawing(@request() req: Request) {
    return await this.drawingRepository.updateDrawing(
      req.params.drawingId,
      req.body,
    );
  }

  @httpDelete('/:drawingId')
  public async deleteDrawing(@request() req: Request) {
    return await this.drawingRepository.deleteDrawing(req.params.drawingId);
  }

  @httpPost('/:drawingId/publish')
  public async publishDrawing(@request() req: Request) {
    return await this.drawingRepository.publishDrawing(req.body);
  }
}
