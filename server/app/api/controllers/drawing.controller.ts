import { TYPES } from "../../domain/constants/types";
import { DrawingRepository } from "../../infrastructure/data_access/repositories/drawing_repository";
import { inject } from "inversify";
import { controller, httpGet, httpPatch, httpPost, request, response } from "inversify-express-utils";
import { Request } from 'express';

@controller('/drawings')
export class DrawingController {
  @inject(TYPES.DrawingRepository) public drawingRepository: DrawingRepository;

  @httpGet('/')
  public async get() {
    return await this.drawingRepository.findAll();
  }

  @httpGet('/:drawingId')
  public async getDrawingById(@request() req: Request) {
    return await this.drawingRepository.findById(req.params.drawingId);
  }

  @httpPost('/')
  public async createDrawing(@request() req: Request) {
    return req.body.ownerModel === 'User' ? await this.drawingRepository.createUserDrawing(req.body) : await this.drawingRepository.createTeamDrawing(req.body);
  }

  @httpPatch('/:drawingId')
  public async updateDrawing(@request() req: Request) {
    return await this.drawingRepository.updateById(req.params.drawingId, req.body);
  }
}