import { NextFunction, Request, Response, Router } from 'express';
import * as Httpstatus from 'http-status-codes';
import { inject, injectable } from 'inversify';
import * as mongo from 'mongodb';
import { DatabaseService } from '../services/database.service';
import { TYPES } from '../types';
import { User } from '../../../client/src/app/interfaces/interface';


@injectable()
export class DatabaseController {

    router: Router;
    col: mongo.Collection;

    constructor(
        @inject(TYPES.DatabaseService) private databaseService: DatabaseService) {
        this.configureRouter();
    }

    private configureRouter(): void {
        this.router = Router();

        // route pour enregistrer un nouveau utilisateur dans la collection
        this.router.post('/userparam', async (req: Request, res: Response, next: NextFunction) => {
            this.databaseService.addUserData(req.body)
                .then(() => {
                    res.sendStatus(Httpstatus.StatusCodes.CREATED).send();
                })
                .catch((error: Error) => {
                    res.status(Httpstatus.StatusCodes.NOT_FOUND).send(error.message);
                });
        });

        // route pour search user with pseudonyme
        this.router.get('/login', async (req: Request, res: Response, next: NextFunction) => {
            this.databaseService.getUserbyPseudo(req.params.pseudo)
                .then((user: User[]) => {
                    res.json(user);
                })
                .catch((error: Error) => {
                    res.status(Httpstatus.StatusCodes.NOT_FOUND).send(error.message);
                });
        });
        
        // route pour find and modify
        this.router.post('/update', async (req: Request, res: Response, next: NextFunction) => {
            this.databaseService.modifyUserData(req.body)
                .then(() => {
                    res.sendStatus(Httpstatus.StatusCodes.CREATED).send();
                })
                .catch((error: Error) => {
                    res.status(Httpstatus.StatusCodes.NOT_FOUND).send(error.message);
                });
        });

    }
}
