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
    userIsLog : string[];


    constructor(
        @inject(TYPES.DatabaseService) private databaseService: DatabaseService) {
        this.configureRouter();
    }

    private configureRouter(): void {
        this.router = Router();

        // route pour enregistrer un nouveau utilisateur dans la collection
        this.router.post('/useraccountcreation', async (req: Request, res: Response, next: NextFunction) => {
            this.databaseService.addUserData(req.body)
                .then(() => {
                    res.sendStatus(Httpstatus.StatusCodes.CREATED).send();
                })
                .catch((error: Error) => {
                    console.log ("why error ? body = "+req.body);
                    res.status(Httpstatus.StatusCodes.NOT_FOUND).send(error.message);
                });
        });

        // route pour chercher un utilisateur a l'aide du pseudonyme  To be modify
        this.router.get('/userdbloginvalidation', async (req: Request, res: Response, next: NextFunction) => {
            this.databaseService.ValidateUserlogin(req.body)
                .then((LogData : string[]) => {
                    res.json(this.userIsLog = LogData);
                })
                .catch((error: Error) => {
                    res.status(Httpstatus.StatusCodes.NOT_FOUND).send(error.message);
                });
        });
        
        // route pour chercher  et modifier les parametre d'un utilisateur 
        this.router.put('/userparameterupdate', async (req: Request, res: Response, next: NextFunction) => {
            this.databaseService.modifyUserData(req.body)
                .then(() => {
                    res.sendStatus(Httpstatus.StatusCodes.CREATED).send();
                })
                .catch((error: Error) => {
                    res.status(Httpstatus.StatusCodes.NOT_FOUND).send(error.message);
                });
        });

         // route pour supprimer un compte utilisateur a partir de son nom
         this.router.delete('/deleteuseraccount', async (req: Request, res: Response, next: NextFunction) => {
            this.databaseService.deleteuseraccount(req.params.pseudonyme)
                .then(() => {
                    res.sendStatus(Httpstatus.StatusCodes.NO_CONTENT).send();
                })
                .catch((error: Error) => {
                    res.status(Httpstatus.StatusCodes.NOT_FOUND).send(error.message);
                });
        });

        // route pour recuperer tous les documents de la collection userparameter
        this.router.get('/getalluser', async (req: Request, res: Response, next: NextFunction) => {
            this.databaseService.getAll()
                .then((users: User[]) => {
                    res.json(users);
                })
                .catch((error: Error) => {
                    res.status(Httpstatus.StatusCodes.NOT_FOUND).send(error.message);
                });
        });
        

    }
}
