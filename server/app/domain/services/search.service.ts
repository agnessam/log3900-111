import { DrawingRepository } from '@app/infrastructure/data_access/repositories/drawing_repository';
import { UserRepository } from '@app/infrastructure/data_access/repositories/user_repository';
import { injectable } from 'inversify';
import { drawingRepository, userRepository } from '../constants/decorators';
import { SearchServiceInterface } from '../interfaces/search-service.interface';

@injectable()
export class SearchService implements SearchServiceInterface {
  @drawingRepository private drawingRepository: DrawingRepository;
  @userRepository private userRepository: UserRepository;

  async search(query: string): Promise<any[]> {
    const users = await this.userRepository.findManyByQuery({
      $or: [
        { username: { $regex: new RegExp(query, 'ig') } },
        { email: { $regex: new RegExp(query, 'ig') } },
        { firstName: { $regex: new RegExp(query, 'ig') } },
        { lastName: { $regex: new RegExp(query, 'ig') } },
      ],
    });

    const drawings = await this.drawingRepository.findManyByQuery({
      name: {
        $regex: new RegExp(query, 'ig'),
      },
    });

    return [...users, ...drawings];
  }
}
