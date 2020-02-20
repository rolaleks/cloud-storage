package cloudstorage;

import cloudstorage.net.FilePackageReader;
import cloudstorage.net.NotEnoughBytesException;
import io.netty.buffer.ByteBuf;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerFilePackageReader extends FilePackageReader {

    protected ClientHandler client;

    public ServerFilePackageReader(ClientHandler client) {
        this.client = client;
    }

    public boolean read(ByteBuf byteBuf) {

        try {
            String name = this.readFileName(byteBuf);
            long readFileSize = this.readFileSize(byteBuf);

            String filePath = "cloud/" + client.getUser().getLogin() + "/" + name;
            Path path = Paths.get(filePath);
            long loadedLen = Files.exists(path) ? Files.size(path) : 0;

            try (FileOutputStream out = new FileOutputStream(filePath);
                 BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(out)) {

                long leftToLoad = readFileSize - loadedLen;
                leftToLoad = byteBuf.readableBytes() > leftToLoad ? leftToLoad : byteBuf.readableBytes();
                loadedLen += leftToLoad;

                byte[] bytes = new byte[(int)leftToLoad];
                byteBuf.readBytes(bytes);
                bufferedOutputStream.write(bytes);
                if (loadedLen >= readFileSize) {
                    return true;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (NotEnoughBytesException | IOException e) {
            return false;
        }

        return false;
    }


}
