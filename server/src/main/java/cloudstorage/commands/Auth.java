package cloudstorage.commands;

import cloudstorage.ClientHandler;
import cloudstorage.db.User;
import cloudstorage.net.CommandPerformable;

public class Auth implements CommandPerformable {

    protected ClientHandler clientHandler;

    public Auth(ClientHandler clientHandler) {

        this.clientHandler = clientHandler;
    }

    /**
     * @param params строка авторизации в формате "login:pass"
     */
    public void perform(String params) {

        String[] loginPass = params.split(":");

        if (loginPass.length != 2) {
            clientHandler.setAuth(false);
            return;
        }

        //ищем юзера в БД
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
