package cloudstorage.helpers;

import java.io.*;
import java.util.Arrays;

public class FileReader {

    private String path;

    private int chunk = 262144;
    private byte[] buffer;
    private long loaded = 0;

    private BufferedInputStream bufferedInputStream;

    public FileReader(String path) throws FileNotFoundException {
        this.path = path;
        buffer = new byte[this.chunk];
        bufferedInputStream = new BufferedInputStream(new FileInputStream(path));
    }

    public byte[] read() throws IOException {
        int len;
        if ((len = bufferedInputStream.read(buffer)) != -1) {
            loaded += len;
            if (len == this.chunk) {
                return buffer;
            } else {
                return Arrays.copyOfRange(buffer, 0, len);
            }
        }
        this.close();
        return new byte[0];
    }

    public long loadedBytes() {
        return loaded;
    }

    public void close() {
        try {
            bufferedInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
