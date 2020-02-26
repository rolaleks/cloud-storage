package cloudstorage;

import cloudstorage.net.CommandPackage;
import cloudstorage.net.FilePackage;
import cloudstorage.net.PackageReadable;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class PackageReader {

    private ClientHandler clientHandler;
    private byte packageType;
    private PackageReadable packageReader;
    private ServerFilePackageReader serverFilePackageReader;
    private ServerCommandPackageReader serverCommandPackageReader;

    public PackageReader(ClientHandler client) {
        this.clientHandler = client;
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
     *
     * @param byteBuf буфер
     */
    private void initPackage(ByteBuf byteBuf) {

        this.packageType = byteBuf.readByte();

        switch (this.packageType) {
            case FilePackage.flag:
                this.packageReader = getServerFilePackageReader();
                break;
            case CommandPackage.flag:
                this.packageReader = getServerCommandPackageReader();
                break;
        }
    }

    private void reset() {
        this.packageType = 0;
        this.packageReader = null;
    }

    /**
     * @return Обработчик файловых пакетов, если обработчик был уже создан, сбрасываем его состояние
     */
    private ServerFilePackageReader getServerFilePackageReader() {
        if (this.serverFilePackageReader == null) {
            this.serverFilePackageReader = new ServerFilePackageReader(this.clientHandler);
        } else {
            this.serverFilePackageReader.reset();
        }
        return this.serverFilePackageReader;
    }

    /**
     * @return Обработчик пакетов команд, если обработчик был уже создан, сбрасываем его состояние
     */
    private ServerCommandPackageReader getServerCommandPackageReader() {
        if (this.serverCommandPackageReader == null) {
            this.serverCommandPackageReader = new ServerCommandPackageReader(this.clientHandler);
        } else {
            this.serverCommandPackageReader.reset();
        }
        return this.serverCommandPackageReader;
    }
}
