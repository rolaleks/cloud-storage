package cloudstorage;

import cloudstorage.net.FilePackageReader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerFilePackageReader extends FilePackageReader {

    protected ClientHandler client;

    public ServerFilePackageReader(ClientHandler client) {
        this.client = client;
    }

    public Path getFilePath(String fileName) {

        return Paths.get("cloud/" + client.getUser().getLogin() + "/" + fileName);
    }
}
