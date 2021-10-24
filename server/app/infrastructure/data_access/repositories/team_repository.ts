import { injectable } from 'inversify';
import { Team,TeamInterface } from '../../../domain/models/teams';
import { GenericRepository } from './generic_repository';


@injectable()
export class TeamRepository extends GenericRepository<TeamInterface> {
  constructor() {
    super(Team);
  }
  
// For custom request

}
