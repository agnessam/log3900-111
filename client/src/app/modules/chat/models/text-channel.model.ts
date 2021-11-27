export interface TextChannel {
    _id: string;
    name: string;
    ownerId: string;
}

export interface TeamChannel extends TextChannel{
    team: string;
}
