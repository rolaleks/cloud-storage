package cloudstorage;

import cloudstorage.net.CommandPackage;
import cloudstorage.net.FilePackage;
import cloudstorage.net.PackageReadable;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class PackageReader {

    private ServerHandler serverHandler;
    private byte packageType;
    private PackageReadable packageReader;

    private ClientFilePackageReader clientFilePackageReader;
    private ClientCommandPackageReader clientCommandPackageReader;

    public PackageReader(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    public void read(ByteBuf byteBuf) throws IOException {
        if (packageType == 0) {
            //Если это начало запроса, то определяем обработчик этого запроса
            initPackage(byteBuf);
        }

        if (this.packageReader.read(byteBuf)) {
            reset();
        }
        if (byteBuf.readableBytes() == 0) {
            byteBuf.release();
        }
    }

    /**
     * Определение типа обработчика для запроса
     * @param byteBuf буфер
     */
    private void initPackage(ByteBuf byteBuf) {
        this.packageType = byteBuf.readByte();
        switch (this.packageType) {
            case FilePackage.flag:
                this.packageReader = getClientFilePackageReader();
                break;
            case CommandPackage.flag:
                this.packageReader = getClientCommandPackageReader();
                break;
        }
    }

    /**
     * сбрасываем состояние PackageReader в исходное, вызывается после завершение считывания всех пактов запроса
     */
    private void reset() {
        this.packageType = 0;
        this.packageReader = null;
    }


    /**
     *
     * @return Обработчик файловых пакетов, если обработчик был уже создан, сбрасываем его состояние
     */
    private ClientFilePackageReader getClientFilePackageReader() {
        if (this.clientFilePackageReader == null) {
            this.clientFilePackageReader = new ClientFilePackageReader(this.serverHandler);
        } else {
            this.clientFilePackageReader.reset();
        }
        return this.clientFilePackageReader;
    }

    /**
     *
     * @return Обработчик пакетов команд, если обработчик был уже создан, сбрасываем его состояние
     */
    private ClientCommandPackageReader getClientCommandPackageReader() {
        if (this.clientCommandPackageReader == null) {
            this.clientCommandPackageReader = new ClientCommandPackageReader(this.serverHandler);
        } else {
            this.clientCommandPackageReader.reset();
        }
        return this.clientCommandPackageReader;
    }
}
