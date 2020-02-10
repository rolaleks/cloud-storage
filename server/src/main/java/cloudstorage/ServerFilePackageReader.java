package cloudstorage;

import cloudstorage.net.FilePackageReader;

import java.io.*;

public class ServerFilePackageReader extends FilePackageReader {

    protected ClientHandler client;

    public ServerFilePackageReader(BufferedInputStream bufferedInputStream, ClientHandler client) {
        super(bufferedInputStream);
        this.client = client;
    }

    public void read() {

        String name = this.readFileName();
        long readFileSize = this.readFileSize();
        long loadedLen = 0;
        try (FileOutputStream out = new FileOutputStream("cloud/" + name);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(out)) {

            byte[] buffer = new byte[1024];
            int len;
            while ((len = bufferedInputStream.read(buffer)) != -1) {
                loadedLen += len;
                bufferedOutputStream.write(buffer);
                if (loadedLen >= readFileSize) {
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
