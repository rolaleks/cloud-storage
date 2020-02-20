package cloudstorage.commands;

import cloudstorage.ClientHandler;
import cloudstorage.db.User;

public class Auth {

    protected String params;
    protected ClientHandler clientHandler;

    public Auth(String params, ClientHandler clientHandler) {

        this.params = params;
        this.clientHandler = clientHandler;
    }

    public void perform() {

        String[] loginPass = params.split(":");

        if (loginPass.length != 2) {
            clientHandler.setAuth(false);
            return;
        }

        User user = User.findOneByLogin(loginPass[0]);

        if (user != null) {
            boolean isAuth = user.getPass().equals(loginPass[1]);
            clientHandler.setAuth(isAuth);
            if(isAuth)
            clientHandler.setUser(user);
            return;
        }

        clientHandler.setAuth(false);
    }
}
