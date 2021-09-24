import { Router } from 'express';
import { injectable } from 'inversify';

@injectable()
export class helloWorldController {
	router: Router;

	constructor() {
		this.configureRouter();
	}

	private configureRouter(): void {
		this.router = Router();

		this.router.get('/', (req, res) => {
			res.send('Hello World');
		});
	}
}
