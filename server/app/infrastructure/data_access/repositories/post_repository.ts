import {
  PublishedDrawing,
  PublishedDrawingInterface,
} from '@app/domain/models/PublishedDrawing';
import { injectable } from 'inversify';
import { GenericRepository } from './generic_repository';

@injectable()
export class PostRepository extends GenericRepository<PublishedDrawingInterface> {
  constructor() {
    super(PublishedDrawing);
  }
}
