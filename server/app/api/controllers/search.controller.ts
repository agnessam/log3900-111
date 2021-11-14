import { searchService } from '../../domain/constants/decorators';
import { SearchService } from '../../domain/services/search.service';
import { controller, httpGet, queryParam } from 'inversify-express-utils';

@controller('/search')
export class SearchController {
  @searchService private searchService: SearchService;

  @httpGet('/')
  public search(@queryParam('q') query: string) {
    return this.searchService.search(query);
  }
}
