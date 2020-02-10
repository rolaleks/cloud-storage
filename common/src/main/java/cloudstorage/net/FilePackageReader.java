package cloudstorage.net;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import java.io.*;

abstract public class FilePackageReader {

    protected BufferedInputStream bufferedInputStream;

    public FilePackageReader(BufferedInputStream bufferedInputStream) {
        this.bufferedInputStream = bufferedInputStream;
    }

    abstract public void read();

    protected int readFileNameSize() {
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


    protected String readFileName() {
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


    protected long readFileSize() {
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
