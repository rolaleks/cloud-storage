package cloudstorage.commands;

import cloudstorage.ServerHandler;

public class BaseCommand {

    protected String params;
    protected ServerHandler serverHandler;

    public BaseCommand(String params, ServerHandler serverHandler) {
        this.params = params;
        this.serverHandler = serverHandler;
    }
}
