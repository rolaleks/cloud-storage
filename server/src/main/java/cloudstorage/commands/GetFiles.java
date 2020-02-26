package cloudstorage.commands;

import cloudstorage.ClientHandler;
import cloudstorage.db.User;
import cloudstorage.helpers.FolderReader;
import cloudstorage.net.CommandPackage;
import cloudstorage.net.CommandPerformable;
import cloudstorage.net.PackageCommandType;


public class GetFiles implements CommandPerformable {

    protected ClientHandler clientHandler;

    public GetFiles(ClientHandler clientHandler) {

        this.clientHandler = clientHandler;
    }

    /**
     * Получение списка файлов на сервере
     * @param
     */
    public void perform(String params) {

        User user = clientHandler.getUser();
        String path = "cloud/" + user.getLogin();
        FolderReader folderReader = new FolderReader(path);
        folderReader.createPath();
        String[] files = folderReader.getFileNames();

        CommandPackage authPackage = new CommandPackage(PackageCommandType.GET_FILES, String.join(";", files));
        this.clientHandler.send(authPackage);
    }
}
