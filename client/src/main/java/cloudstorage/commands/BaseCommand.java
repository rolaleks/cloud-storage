package cloudstorage.commands;

import cloudstorage.ServerHandler;

public class BaseCommand {

    protected ServerHandler serverHandler;

    public BaseCommand(ServerHandler serverHandler) {

        this.serverHandler = serverHandler;
    }
}
