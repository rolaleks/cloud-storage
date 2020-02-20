package cloudstorage.commands;

import cloudstorage.ClientHandler;
import cloudstorage.db.User;
import cloudstorage.helpers.FolderReader;
import cloudstorage.net.CommandPackage;
import cloudstorage.net.CommandPerformable;
import cloudstorage.net.FilePackage;
import cloudstorage.net.PackageCommandType;


public class GetFile  implements CommandPerformable {

    protected ClientHandler clientHandler;

    public GetFile(ClientHandler clientHandler) {

        this.clientHandler = clientHandler;
    }

    public void perform(String params) {

        User user = clientHandler.getUser();
        String path = "cloud/" + user.getLogin();
        FilePackage filePackage = new FilePackage(path + "/" + params);
        this.clientHandler.send(filePackage);
    }
}
