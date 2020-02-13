package cloudstorage;

import cloudstorage.net.FilePackageReader;
import cloudstorage.net.NotEnoughBytesException;
import io.netty.buffer.ByteBuf;

import java.io.*;

public class ServerFilePackageReader extends FilePackageReader {

    protected ClientHandler client;

    public ServerFilePackageReader(ClientHandler client) {
        this.client = client;
    }

    public boolean read(ByteBuf byteBuf) {

        try {
            String name = this.readFileName(byteBuf);
            long readFileSize = this.readFileSize(byteBuf);
            long loadedLen = 0;
            System.out.println(name);
            try (FileOutputStream out = new FileOutputStream("cloud/" + name);
                 BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(out)) {
                loadedLen += byteBuf.readableBytes();
                bufferedOutputStream.write(byteBuf.array());
                if (loadedLen >= readFileSize) {
                    return true;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (NotEnoughBytesException e) {
            return false;
        }

        return false;
    }


}
