package cloudstorage;

import cloudstorage.net.CommandPackage;
import cloudstorage.net.FilePackage;
import cloudstorage.net.PackageReadable;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class PackageReader {

    private ClientHandler clientHandler;
    private byte packageType;
    private PackageReadable packageReader;

    public PackageReader(ClientHandler client) {
        this.clientHandler = client;
    }

    public void read(ByteBuf byteBuf) throws IOException {
        if (packageType == 0) {
            System.out.println("initPackage");
            initPackage(byteBuf);
        }

        if (this.packageReader.read(byteBuf)) {
            System.out.println("reset");
            reset();
        }
    }

    private void initPackage(ByteBuf byteBuf) {
        this.packageType = byteBuf.readByte();
        if (this.packageType == FilePackage.flag) {
            this.packageReader = new ServerFilePackageReader(this.clientHandler);
        } else if (this.packageType == CommandPackage.flag) {
            this.packageReader = new ServerCommandPackageReader(this.clientHandler);
        }
    }

    private void reset() {
        this.packageType = 0;
        this.packageReader = null;
    }
}
