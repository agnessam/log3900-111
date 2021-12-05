import { DrawingRepository } from '../../infrastructure/data_access/repositories/drawing_repository';
import { TeamRepository } from '../../infrastructure/data_access/repositories/team_repository';
import { UserRepository } from '../../infrastructure/data_access/repositories/user_repository';
import { inject, injectable } from 'inversify';
import {
  drawingRepository,
  teamRepository,
  userRepository,
} from '../constants/decorators';
import { SearchServiceInterface } from '../interfaces/search-service.interface';
import { DrawingInterface } from '../models/Drawing';
import { TeamInterface } from '../models/teams';
import { UserInterface } from '../models/user';
import { TYPES } from '../constants/types';
import { CollaborationTrackerService } from './collaboration-tracker.service';

const MONTH = {
  january: 0,
  february: 1,
  march: 2,
  april: 3,
  may: 4,
  june: 5,
  july: 6,
  august: 7,
  september: 8,
  october: 9,
  november: 10,
  december: 11,
};

@injectable()
export class SearchService implements SearchServiceInterface {
  @drawingRepository private drawingRepository: DrawingRepository;
  @teamRepository private teamRepository: TeamRepository;
  @userRepository private userRepository: UserRepository;

  @inject(TYPES.CollaborationTrackerService)
  private collaborationTrackerService: CollaborationTrackerService;

  async search(query: string): Promise<any> {
    const date = this.extractDate(query);
    console.log(date);

    // Find with username
    const users = await this.userRepository.findManyByQuery({
      $or: [
        {
          username: { $regex: new RegExp(query, 'ig') },
        },
        {
          email: { $regex: new RegExp(query, 'ig') },
          'privacySetting.searchableByEmail': true,
        },
        {
          firstName: { $regex: new RegExp(query, 'ig') },
          'privacySetting.searchableByFirstName': true,
        },
        {
          lastName: { $regex: new RegExp(query, 'ig') },
          'privacySetting.searchableByLastName': true,
        },
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

    const drawingsFromOwners =
      await this.drawingRepository.findManyDrawingsById(
        matchingDrawingIds as string[],
      );

    let drawingQuery: any = {
      $or: [{ name: { $regex: new RegExp(query, 'ig') } }],
    };

    if (date) {
      const endDate = new Date(date.getFullYear(), date.getMonth() + 1);
      drawingQuery.$or.push({ createdAt: { $gte: date, $lte: endDate } });
    }

    let drawings = await this.drawingRepository.findManyDrawingsByQuery(
      drawingQuery,
    );

    drawings = this.removeDuplicates([...drawings, ...drawingsFromOwners]);

    const drawingToCollaborators: {} =
      this.collaborationTrackerService.getDrawingCollaborators();

    drawings.map((drawing) => {
      if (drawingToCollaborators[drawing._id] != null) {
        drawing.set('collaborators', drawingToCollaborators[drawing._id], {
          strict: false,
        });
      } else {
        drawing.set('collaborators', [], { strict: false });
      }

      return drawing;
    });

    return {
      users: [...users],
      teams: [...teams],
      drawings: drawings,
    };
  }

  private getDrawingIds(owners: UserInterface[] | TeamInterface[]) {
    return owners
      .map((owner) => owner.drawings)
      .reduce((p, c) => [...p, ...c], []);
  }

  private removeDuplicates(items: DrawingInterface[]): DrawingInterface[] {
    const uniqueDrawings = [];
    const hasSeen = new Set();

    const len = items.length;

    for (let i = 0; i < len; ++i) {
      const item = items[i];
      if (hasSeen.has(item._id.toString())) {
        continue;
      }
      uniqueDrawings.push(item);
      hasSeen.add(item._id.toString());
    }

    return uniqueDrawings;
  }

  private extractDate(query: string): Date | null {
    // Do a split to check if it's in a 22 november 2021 or november 22 format.
    let date: Date | null;
    const items = query.split(' ');
    date = this.extractNameDate(items);
    return date;

    const dateItems = query.split('-');
    console.log(dateItems);

    // Check if it's a month
    let month = null;
    if (query.toLowerCase() in MONTH) {
      month = MONTH[query];
      return new Date(2021, month);
    }

    return null;
  }

  private extractNameDate(items: string[]): Date | null {
    if (items.length == 1) {
      if (items[0].toLowerCase() in MONTH) {
        console.log(MONTH[items[0].toLowerCase()]);
        return new Date(2021, MONTH[items[0].toLowerCase()]);
      }
    } else if (items.length == 2) {
      let month: [number, number] | null = this.getMonthFromQuery(items);
      if (!month) return null; // Invalid date found
      return new Date(
        2021,
        month[0],
        parseInt(items[items.length - month[1] - 1]),
      );
    }
    return null;
  }

  // Takes in a splitted string in any AAAA-MM-DD or DD-MM-AAAA and returns the month
  private getMonthFromQuery(items: string[]): [number, number] | null {
    for (let i = 0; i < items.length; ++i) {
      if (items[i].toLowerCase() in MONTH) {
        return [MONTH[items[i].toLowerCase()], i];
      }
    }
    return null;
  }
}
