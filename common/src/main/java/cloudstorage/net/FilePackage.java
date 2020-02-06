package cloudstorage.net;

import cloudstorage.helpers.FileReader;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilePackage extends Package {

    public static byte flag = 10;

    private String file;
    private String filename;
    private long size;
    private FileReader fileReader;

    public FilePackage(String file) {
        this.file = file;
        Path filepath = Paths.get(file);
        filename = filepath.getFileName().toString();

        try {
            fileReader = new FileReader(file);
            size = Files.size(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    byte[] getBodyBytes() {

        try {
            return fileReader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new byte[0];
    }

    @Override
    byte[] getHeadBytes() {

        byte[] nameSizeBytes = Ints.toByteArray(this.filename.length());
        byte[] nameBytes = this.filename.getBytes();
        byte[] sizeBytes = Longs.toByteArray(this.size);


        return Bytes.concat(nameSizeBytes, nameBytes, sizeBytes);
    }

    @Override
    byte getFlag() {
        return flag;
    }
}
