// Parametre de chaque dessin dessin
export interface IDrawing {
         drawingId: string,
         owner:string[],
         creationDate: Date,
         canvas: string, // to be change according to the code
         email: string,
         firstName: string,
         lastName: string,   
         drawingVisibility: string,    // --protected
         ProtectedAccesCode: string | null, // proteted code to modify drawing appear only when protected choose
         avatar : string,
         drawingName : string;

}

export interface CollaborationTeam {
     teamId: string,
     teamName: string,
     members: string[],
     drawings: string[],
     drawingVisibility: string,    // --protected will be a drop down field  
}


export interface User {
     pseudonyme: string,
     userId: string,
     avatar: string , 
     description: string ,
     publishedDrawings: string[] ,
     followers: string[] ,
     following: string[] ,
     connectionHistory: ConnectionLog[] ,
     editionHistory: EditionLog[],
     timestamps : {createdAt: 'created_at', updatedAt: 'updated_at'},
     email: string,
     password: string,
     firstName: string,
     lastName: string,
}


export interface ConnectionLog {
    LastConnexiontimestamp : Date,
    LastDeconnexiontimestamp: Date,
}
export interface EditionLog {
    drawingId: string, // not sure 
    drawingName : string;
    timestamp: Date,
    owner : string[],
}
