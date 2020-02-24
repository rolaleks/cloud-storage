package cloudstorage.helpers;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FolderReader {

    private String path;

    public FolderReader(String path){
        this.path = path;
    }


    public String[] getFileNames() {
        try {
            String[] files = Files.list(Paths.get(path))
                    .filter(Files::isRegularFile)
                    .map(path -> path.getFileName().toString())
                    .toArray(String[]::new);
            return files;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String[0];
    }

    public void createPath() {

        Path folder = Paths.get(path);
        if (!Files.exists(folder)) {
            try {
                Files.createDirectories(folder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
