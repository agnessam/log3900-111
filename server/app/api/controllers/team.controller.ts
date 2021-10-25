import { TYPES } from '@app/domain/constants/types';
import { TeamRepository } from '@app/infrastructure/data_access/repositories/team_repository';
import { Request} from 'express';
import { inject } from 'inversify';
import {
  controller,
  httpDelete,
  httpGet,
  httpPatch,
  httpPost,
  request,
} from 'inversify-express-utils';
 import passport from 'passport';

@controller('/teams', passport.authenticate('jwt', { session: false }))
export class TeamController {
  @inject(TYPES.TeamRepository) public teamRepository: TeamRepository;

  @httpGet('/')
  public async get() {
    return await this.teamRepository.findAll();
  }

  @httpGet('/:id')
  public async getTeamById(@request() req: Request) {
    return await this.teamRepository.findById(req.params.id);
  }

  @httpPost('/')
  public async createTeam(@request() req: Request) {
    return await this.teamRepository.create(req.body);
  }

  @httpPatch('/:id')
  public async updateTeam(@request() req: Request) {
    return await this.teamRepository.updateById(req.params.id, req.body);
  }

  @httpDelete('/:id')
  public async deleteTeam(@request() req: Request) {
    return await this.teamRepository.deleteById(req.params.id);
  }


}
