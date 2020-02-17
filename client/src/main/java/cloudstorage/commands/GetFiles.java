package cloudstorage.commands;

import cloudstorage.ControllerManager;
import cloudstorage.ServerHandler;

public class GetFiles extends BaseCommand {

    public GetFiles(String params, ServerHandler clientHandler) {
        super(params, clientHandler);
    }

    public void perform() {

        String[] files = params.split(";");
        ControllerManager.getMainController().setRemoteFiles(files);
    }
}
