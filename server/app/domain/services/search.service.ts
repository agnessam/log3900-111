import { DrawingRepository } from '@app/infrastructure/data_access/repositories/drawing_repository';
import { TeamRepository } from '@app/infrastructure/data_access/repositories/team_repository';
import { UserRepository } from '@app/infrastructure/data_access/repositories/user_repository';
import { injectable } from 'inversify';
import {
  drawingRepository,
  teamRepository,
  userRepository,
} from '../constants/decorators';
import { SearchServiceInterface } from '../interfaces/search-service.interface';
import { TeamInterface } from '../models/teams';
import { UserInterface } from '../models/user';

@injectable()
export class SearchService implements SearchServiceInterface {
  @drawingRepository private drawingRepository: DrawingRepository;
  @teamRepository private teamRepository: TeamRepository;
  @userRepository private userRepository: UserRepository;

  async search(query: string): Promise<any> {
    const users = await this.userRepository.findManyByQuery({
      $or: [
        { username: { $regex: new RegExp(query, 'ig') } },
        { email: { $regex: new RegExp(query, 'ig') } },
        { firstName: { $regex: new RegExp(query, 'ig') } },
        { lastName: { $regex: new RegExp(query, 'ig') } },
      ],
    });

    const teams = await this.teamRepository.findManyByQuery({
      $or: [{ name: { $regex: new RegExp(query, 'ig') } }],
    });

    const matchingUsersDrawingsIds = this.getDrawingIds(users);
    const matchingTeamsDrawingsIds = this.getDrawingIds(teams);

    const matchingDrawingIds = [
      ...matchingUsersDrawingsIds,
      ...matchingTeamsDrawingsIds,
    ];

    const drawingsFromOwners = await this.drawingRepository.findManyById(
      matchingDrawingIds,
    );

    const drawings = await this.drawingRepository.findManyByQuery({
      name: {
        $regex: new RegExp(query, 'ig'),
      },
    });

    return {
      users: [...users],
      teams: [...teams],
      drawings: [...new Set([...drawings, ...drawingsFromOwners])],
    };
  }

  private getDrawingIds(owners: UserInterface[] | TeamInterface[]) {
    return owners
      .map((owner) => owner.drawings)
      .reduce((p, c) => [...p, ...c], []);
  }
}
