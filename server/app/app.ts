import cors from 'cors';
import express from 'express';
import { injectable } from 'inversify';

@injectable()
export class Application {
    private readonly internalError: number = 500;
    app: express.Application;
    
    constructor() {
        this.app = express();
        this.config();
    }

    // Middleware configuration
    private config(): void {
        this.app.use(cors());
    }
}
