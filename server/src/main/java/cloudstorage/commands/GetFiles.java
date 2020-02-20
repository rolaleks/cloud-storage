package cloudstorage.commands;

import cloudstorage.ClientHandler;
import cloudstorage.db.User;
import cloudstorage.helpers.FolderReader;
import cloudstorage.net.CommandPackage;
import cloudstorage.net.PackageCommandType;


public class GetFiles {

    protected ClientHandler clientHandler;

    public GetFiles(ClientHandler clientHandler) {

        this.clientHandler = clientHandler;
    }

    public void perform() {

        User user = clientHandler.getUser();
        String path = "cloud/" + user.getLogin();
        FolderReader folderReader = new FolderReader(path);
        folderReader.createPath();
        String[] files = folderReader.getFileNames();

        CommandPackage authPackage = new CommandPackage(PackageCommandType.GET_FILES, String.join(";", files));
        this.clientHandler.send(authPackage);
    }
}
