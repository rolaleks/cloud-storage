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

    public final static byte flag = 10;

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

    /**
     *
     * @return байты файла, максимальный размер массива = fileReader.chunk , если массив пустой, то прочитан весь файл
     */
    @Override
    byte[] getBodyBytes() {

        try {
            return fileReader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        fileReader.close();

        return new byte[0];
    }

    /**
     * @return список байтов шапки пакета
     */
    @Override
    byte[] getHeadBytes() {

        byte[] nameBytes = this.filename.getBytes();
        byte[] nameSizeBytes = Ints.toByteArray(nameBytes.length);
        byte[] sizeBytes = Longs.toByteArray(this.size);


        return Bytes.concat(nameSizeBytes, nameBytes, sizeBytes);
    }

    @Override
    byte getFlag() {
        return flag;
    }
}
