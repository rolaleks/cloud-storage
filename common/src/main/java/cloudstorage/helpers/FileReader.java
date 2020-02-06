package cloudstorage.helpers;

import java.io.*;
import java.util.Arrays;

public class FileReader {

    private String path;

    private int chunk = 262144;

    private BufferedInputStream bufferedInputStream;

    public FileReader(String path) throws FileNotFoundException {
        this.path = path;
        bufferedInputStream = new BufferedInputStream(new FileInputStream(path));
    }

    public byte[] read() throws FileNotFoundException, IOException {
        byte[] buffer = new byte[this.chunk];
        int len;
        if ((len = bufferedInputStream.read(buffer)) != -1) {
            if (len == this.chunk) {
                return buffer;
            } else {
                return Arrays.copyOfRange(buffer, 0, len);
            }
        }

        return new byte[0];
    }

    public int getChunk() {
        return chunk;
    }

    public void setChunk(int chunk) {
        this.chunk = chunk;
    }

    public void close() {
        try {
            bufferedInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
