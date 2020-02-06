package cloudstorage;

import cloudstorage.net.FilePackage;
import com.google.common.primitives.Ints;

import java.io.BufferedInputStream;
import java.io.IOException;

public class PackageReader {

    private BufferedInputStream bufferedInputStream;

    public PackageReader(BufferedInputStream bufferedInputStream) {
        this.bufferedInputStream = bufferedInputStream;
    }

    public void read() throws IOException {

        int flag = bufferedInputStream.read();
        System.out.println(flag);
        if (flag == FilePackage.flag) {
            new FilePackageReader(bufferedInputStream).read();
        }
    }
}
