import { TYPES } from '@app/domain/constants/types';
import { TeamRepository } from '@app/infrastructure/data_access/repositories/team_repository';
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
import passport from 'passport';

@controller('/teams', passport.authenticate('jwt', { session: false }))
export class TeamController {
  @inject(TYPES.TeamRepository) public teamRepository: TeamRepository;

  @httpGet('/')
  public async get() {
    return await this.teamRepository.findAll();
  }

  @httpGet('/:teamId')
  public async getTeamById(@request() req: Request) {
    return await this.teamRepository.findById(req.params.teamId);
  }

  @httpPost('/')
  public async createTeam(@request() req: Request) {
    return await this.teamRepository.create(req.body);
  }

  @httpPatch('/:teamId')
  public async updateTeam(@request() req: Request) {
    return await this.teamRepository.updateById(req.params.teamId, req.body);
  }

  @httpDelete('/:teamId')
  public async deleteTeam(@request() req: Request) {
    return await this.teamRepository.deleteById(req.params.teamId);
  }

  // Join a collaboration team
  @httpPost('/:teamId/join')
  public async addMemberToTeam(@request() req: Request) {
    return await this.teamRepository.addMemberToTeam(
      req.params.teamId,
      req.body,
    );
  }

  @httpGet('/:teamId/members')
  public async getTeamMembers(@request() req: Request) {
    return await this.teamRepository.getTeamMembers(req.params.teamId);
  }

  @httpGet('/:teamId/drawings')
  public async getTeamDrawings(@request() req: Request) {
    return await this.teamRepository.getTeamDrawings(req.params.teamId);
  }
}
