package cloudstorage;

import cloudstorage.net.FilePackageReader;
import io.netty.buffer.ByteBuf;

import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientFilePackageReader extends FilePackageReader {

    protected ServerHandler serverHandler;

    public ClientFilePackageReader(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    @Override
    public boolean read(ByteBuf byteBuf) {
        boolean result = super.read(byteBuf);

        if (result) {
            //если файл полностью получен, то обновляем список файлов на клиенте
            ControllerManager.getMainController().refreshFiles();
        }
        return result;
    }

    @Override
    public Path getFilePath(String fileName) {

        return Paths.get(ControllerManager.getMainController().getFolder() + "/" + fileName);
    }
}
