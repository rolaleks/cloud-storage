package cloudstorage;

import cloudstorage.net.CommandPackage;
import cloudstorage.net.FilePackage;

import java.io.BufferedInputStream;
import java.io.IOException;

public class PackageReader {

    private BufferedInputStream bufferedInputStream;
    private ClientHandler clientHandler;

    public PackageReader(BufferedInputStream bufferedInputStream, ClientHandler client) {
        this.bufferedInputStream = bufferedInputStream;
        this.clientHandler = client;
    }

    public void read() throws IOException {

        int flag = bufferedInputStream.read();
        System.out.println(flag);
        if (flag == FilePackage.flag) {
            new ServerFilePackageReader(bufferedInputStream, clientHandler).read();
        } else if (flag == CommandPackage.flag) {
            new ServerCommandPackageReader(bufferedInputStream, clientHandler).perform();
        }

    }
}
