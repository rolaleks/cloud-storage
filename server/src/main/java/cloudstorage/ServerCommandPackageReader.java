package cloudstorage;

import cloudstorage.commands.Auth;
import cloudstorage.net.CommandPackageReader;
import cloudstorage.net.PackageCommandType;

import java.io.*;

public class ServerCommandPackageReader extends CommandPackageReader {

    protected ClientHandler client;

    public ServerCommandPackageReader(BufferedInputStream bufferedInputStream, ClientHandler client) {
        super(bufferedInputStream);
        this.client = client;
    }

    public void perform() {

        String name = this.readCommandName();
        int readFileSize = this.readParamsSize();
        String params = this.readParams(readFileSize);

        switch (PackageCommandType.valueOf(name)) {
            case AUTH:
                (new Auth(params, this.client)).perform();
                break;
            default:
                throw new IllegalStateException("Unknown command: " + name);
        }

    }

    private String readParams(int size) {

        byte[] params = new byte[size];
        try {
            if (bufferedInputStream.read(params) == size) {
                return new String(params);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
