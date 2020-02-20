package cloudstorage.commands;

import cloudstorage.ServerHandler;

public class AuthStatus extends BaseCommand {


    public AuthStatus(String params, ServerHandler clientHandler) {
        super(params, clientHandler);
    }

    public void perform() {

        boolean isAuth = params.equals("1");

        serverHandler.getCloudClient().setAuth(isAuth);
    }
}
