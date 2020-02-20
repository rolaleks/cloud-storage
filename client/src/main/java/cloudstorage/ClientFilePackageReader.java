package cloudstorage;

import cloudstorage.net.FilePackageReader;
import cloudstorage.net.NotEnoughBytesException;
import io.netty.buffer.ByteBuf;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientFilePackageReader extends FilePackageReader {

    protected ServerHandler serverHandler;

    public ClientFilePackageReader(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }


    @Override
    public Path getFilePath(String fileName) {

        return Paths.get(CloudClient.localPath + "/" + fileName);
    }
}
