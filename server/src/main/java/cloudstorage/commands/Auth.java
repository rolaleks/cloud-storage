package cloudstorage.commands;

import cloudstorage.ClientHandler;

public class Auth {

    protected String params;
    protected ClientHandler clientHandler;

    public Auth(String params, ClientHandler clientHandler) {

        this.params = params;
        this.clientHandler = clientHandler;
    }

    public void perform() {

        //TODO авторизацию

        clientHandler.setAuth(params.startsWith("test:test"));
    }
}
