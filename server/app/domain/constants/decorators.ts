import { inject } from 'inversify';
import { TYPES } from './types';

export const drawingRepository = inject(TYPES.DrawingRepository);
export const userRepository = inject(TYPES.UserRepository);
export const searchService = inject(TYPES.SearchService);
