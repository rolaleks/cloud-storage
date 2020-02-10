package cloudstorage.net;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import java.io.BufferedInputStream;
import java.io.IOException;

abstract public class CommandPackageReader {

    protected BufferedInputStream bufferedInputStream;

    public CommandPackageReader(BufferedInputStream bufferedInputStream) {
        this.bufferedInputStream = bufferedInputStream;
    }

    abstract public void perform();

    protected int readCommandNameSize() {
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


    protected String readCommandName() {
        int size = this.readCommandNameSize();
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


    protected int readParamsSize() {
        byte[] fileSize = new byte[4];
        try {
            if (bufferedInputStream.read(fileSize) == 4) {
                return Ints.fromByteArray(fileSize);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
