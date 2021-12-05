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
      let endDate: Date;
      const dateQueried = date[0];
      const dateType = date[1];
      if (dateType == 'day') {
        endDate = new Date(
          dateQueried.getFullYear(),
          dateQueried.getMonth(),
          dateQueried.getDate() + 1,
        );
      } else if (dateType == 'month') {
        endDate = new Date(
          dateQueried.getFullYear(),
          dateQueried.getMonth() + 1,
        );
      } else {
        endDate = new Date(dateQueried.getFullYear() + 1, 0);
      }
      drawingQuery.$or.push({ createdAt: { $gte: date[0], $lte: endDate } });
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

  private extractDate(query: string): [Date, string] | null {
    // Do a split to check if it's in a 22 november 2021 or november 22 format.
    let date: [Date, string] | null;

    const hyphenDate = query.split('-');
    date = this.extractSymbolDate(hyphenDate);
    if (date != null && date[0] != null) return date;

    const slashDate = query.split('/');
    date = this.extractSymbolDate(slashDate);
    if (date != null && date[0] != null) return date;

    const whitespaceDate = query.split(' ');
    date = this.extractNameDate(whitespaceDate);

    if (date != null && date[0] != null) return date;

    return null;
  }

  private extractSymbolDate(items: string[]): [Date, string] | null {
    if (items.length != 3) return null;

    // Check if it's AAAA-MM-DD or DD-MM-AAAA
    const validFormatAndType = this.validateSymbolDateFormat(items);
    if (validFormatAndType == null) return null;

    let year: number;
    let month: number;
    let day: number;

    if (validFormatAndType == '2') {
      year = parseInt(items[2]);
      month = parseInt(items[1]) - 1;
      day = parseInt(items[0]);

      return [new Date(year, month, day), 'day'];
    }

    if (validFormatAndType == '1') {
      year = parseInt(items[0]);
      month = parseInt(items[1]) - 1;
      day = parseInt(items[2]);

      return [new Date(year, month, day), 'day'];
    }
    return null;
  }

  private extractNameDate(items: string[]): [Date, string] | null {
    if (items.length == 1) {
      if (items[0].toLowerCase() in MONTH) {
        return [new Date(2021, MONTH[items[0].toLowerCase()]), 'month'];
      }
      // Check if its a year
      const year = parseInt(items[0]);
      if (year > 1979 && year < 2022) {
        return [new Date(year, 0), 'year'];
      }
    } else if (items.length == 2) {
      let month: [number, number] | null = this.getMonthFromQuery(items);
      if (!month) return null; // Invalid date found
      const dayOrYear = parseInt(items[items.length - month[1] - 1]);
      if (dayOrYear > 1979) {
        return [new Date(dayOrYear, month[0]), 'month'];
      }
      if (dayOrYear < 0 || dayOrYear > 31) return null;
      return [new Date(2021, month[0], dayOrYear), 'day'];
    } else if (items.length == 3) {
      let month: [number, number] | null = this.getMonthFromQuery(items);
      if (!month) return null;
      items.splice(month[1], 1);
      let year: [number, number] | null = this.getYearFromQuery(items);
      if (!year) return null;
      const day = parseInt(items[items.length - year[1] - 1]);
      if (day < 0 || day > 31) return null;
      return [new Date(year[0], month[0], day), 'day'];
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

  private getYearFromQuery(items: string[]): [number, number] | null {
    for (let i = 0; i < items.length; ++i) {
      const entry = parseInt(items[i]);
      if (entry > 1979 && entry < 2022) {
        return [entry, i];
      }
    }
    return null;
  }

  private validateSymbolDateFormat(items: string[]): string | null {
    let year = parseInt(items[0]);
    let month = parseInt(items[1]);
    let day = parseInt(items[2]);

    if (year > 1979) {
      if (month > 0 && month <= 12) {
        if (day > 0 && day < 31) {
          return '1';
        }
      }
    }
    year = parseInt(items[2]);
    month = parseInt(items[1]);
    day = parseInt(items[0]);
    if (year > 1979) {
      if (month > 0 && month <= 12) {
        if (day > 0 && day < 31) {
          return '2';
        }
      }
    }
    return null;
  }
}
