package cloudstorage.commands;

import cloudstorage.ControllerManager;
import cloudstorage.ServerHandler;
import cloudstorage.net.CommandPerformable;

public class GetFiles extends BaseCommand implements CommandPerformable {

    public GetFiles(ServerHandler serverHandler) {
        super(serverHandler);
    }

    /**
     *
     * @param params список файлов с сервера разделенные ";"
     */
    public void perform(String params) {

        String[] files = params.split(";");
        ControllerManager.getMainController().setRemoteFiles(files);
    }
}
