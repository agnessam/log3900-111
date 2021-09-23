import { Container } from "inversify";
import { Application } from "./app";
import { Server } from "./server";
import { TYPES } from "./types";

export const containerBootstrapper: () => Promise<Container>  = async() => {
    const container: Container = new Container();

    container.bind(TYPES.Server).to(Server);
    container.bind(TYPES.Application).to(Application);

    return container;
}
