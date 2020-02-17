package cloudstorage;

import cloudstorage.net.CommandPackage;
import cloudstorage.net.FilePackage;
import cloudstorage.net.PackageReadable;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class PackageReader {

    private ServerHandler serverHandler;
    private byte packageType;
    private PackageReadable packageReader;

    public PackageReader(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
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
            this.packageReader = new ClientFilePackageReader(this.serverHandler);
        } else if (this.packageType == CommandPackage.flag) {
            this.packageReader = new ClientCommandPackageReader(this.serverHandler);
        }
    }

    private void reset() {
        this.packageType = 0;
        this.packageReader = null;
    }
}
