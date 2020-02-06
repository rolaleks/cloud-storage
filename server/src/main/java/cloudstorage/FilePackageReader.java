package cloudstorage;

import com.google.common.base.Strings;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import java.io.*;

public class FilePackageReader {

    private BufferedInputStream bufferedInputStream;

    public FilePackageReader(BufferedInputStream bufferedInputStream) {
        this.bufferedInputStream = bufferedInputStream;
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

    private int readFileNameSize() {
        byte[] fileNameSize = new byte[4];
        try {
            if (bufferedInputStream.read(fileNameSize) == 4) {
                return Ints.fromByteArray(fileNameSize);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }


    private String readFileName() {
        int size = this.readFileNameSize();
        byte[] fileName = new byte[size];
        try {
            if (bufferedInputStream.read(fileName) == size) {
                return new String(fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    private long readFileSize() {
        byte[] fileSize = new byte[8];
        try {
            if (bufferedInputStream.read(fileSize) == 8) {
                return Longs.fromByteArray(fileSize);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
