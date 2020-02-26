package cloudstorage.commands;

import cloudstorage.ServerHandler;
import cloudstorage.net.CommandPerformable;
import cloudstorage.net.PackageCommandType;

/**
 * Диспечиризация обработчиков команд на клиенте и их кеширование
 */
public class ClientCommandDispatcher {

    private ServerHandler serverHandler;
    private AuthStatus authStatus;
    private GetFiles getFiles;

    public ClientCommandDispatcher(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    public CommandPerformable getCommand(PackageCommandType type) {

        switch (type) {
            case AUTH:
                return getAuthStatus();
            case GET_FILES:
                return getGetFiles();
        }

        throw new IllegalStateException("Unknown client command: " + type);
    }

    public AuthStatus getAuthStatus() {
        if (authStatus == null) {
            authStatus = new AuthStatus(this.serverHandler);
        }
        return authStatus;
    }

    public GetFiles getGetFiles() {
        if (getFiles == null) {
            getFiles = new GetFiles(this.serverHandler);
        }
        return getFiles;
    }
}
