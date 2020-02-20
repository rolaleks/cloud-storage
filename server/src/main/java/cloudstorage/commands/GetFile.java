package cloudstorage.commands;

import cloudstorage.ClientHandler;
import cloudstorage.db.User;
import cloudstorage.helpers.FolderReader;
import cloudstorage.net.CommandPackage;
import cloudstorage.net.FilePackage;
import cloudstorage.net.PackageCommandType;


public class GetFile {

    protected String params;
    protected ClientHandler clientHandler;

    public GetFile(String params, ClientHandler clientHandler) {

        this.params = params;
        this.clientHandler = clientHandler;
    }

    public void perform() {

        User user = clientHandler.getUser();
        String path = "cloud/" + user.getLogin();
        FilePackage filePackage = new FilePackage(path + "/" + params);
        this.clientHandler.send(filePackage);
    }
}
