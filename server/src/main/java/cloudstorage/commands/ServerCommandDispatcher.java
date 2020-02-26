package cloudstorage.commands;

import cloudstorage.ClientHandler;
import cloudstorage.net.CommandPerformable;
import cloudstorage.net.PackageCommandType;

/**
 * Диспечиризация обработчиков команд на сервере и их кеширование
 */
public class ServerCommandDispatcher {

    private ClientHandler clientHandler;
    private Auth auth;
    private GetFile getFile;
    private GetFiles getFiles;

    public ServerCommandDispatcher(ClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    public CommandPerformable getCommand(PackageCommandType type) {

        switch (type) {
            case AUTH:
                return getAuth();
            case GET_FILE:
                return getGetFile();
            case GET_FILES:
                return getGetFiles();
        }

        throw new IllegalStateException("Unknown server command: " + type);
    }

    public GetFile getGetFile() {
        if (getFile == null) {
            getFile = new GetFile(this.clientHandler);
        }
        return getFile;
    }

    public Auth getAuth() {
        if (auth == null) {
            auth = new Auth(this.clientHandler);
        }
        return auth;
    }

    public GetFiles getGetFiles() {
        if (getFiles == null) {
            getFiles = new GetFiles(this.clientHandler);
        }
        return getFiles;
    }
}
