package cloudstorage.net;

import io.netty.buffer.ByteBuf;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

abstract public class FilePackageReader implements PackageReadable {

    protected Integer fileNameSize;
    protected String fileName;
    protected Long fileSize;
    protected boolean fileCreated;


    public boolean read(ByteBuf byteBuf) {

        try {
            //Читаем имя из пакета, если имя уже было считано в предыдущем пакете байт, то берем из кеша
            String name = this.readFileName(byteBuf);
            //Читаем размер файла из пакета, если размер файла уже был считан в предыдущем пакете байт, то берем из кеша
            long totalFileSize = this.readFileSize(byteBuf);

            Path path = getFilePath(name);
            //смотрим сколько мы уже загрузили в файл байт, если fileCreated = false значит это новый запрос на загрузку файла,
            // и мы пишем его с начала файла (перезаписываем файл)
            long currentFileSize = fileCreated && Files.exists(path) ? Files.size(path) : 0;

            //Если нет байт для записи, то открывать поток записи в файл нет смысла
            if (byteBuf.readableBytes() <= 0) {
                return false;
            }

            try (FileOutputStream out = new FileOutputStream(path.toString(), fileCreated);
                 BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(out)) {
                //помечаем, что мы уже начали писать в файл, дальше будем добавлять байты в файл в конец файла
                fileCreated = true;

                //считает сколько осталось байт, для загрузки в файл
                long bytesLeft = totalFileSize - currentFileSize;
                //Если пришел пакет больше чем нам нужно, то берем часть байт и пишем в файл
                bytesLeft = byteBuf.readableBytes() > bytesLeft ? bytesLeft : byteBuf.readableBytes();
                currentFileSize += bytesLeft;

                while (bytesLeft-- > 0) {
                    bufferedOutputStream.write(byteBuf.readByte());
                }

                if (currentFileSize >= totalFileSize) {
                    return true;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (NotEnoughBytesException | IOException e) {
            return false;
        }

        return false;
    }


    /**
     * @param byteBuf буфер
     * @return размер названия файла
     * @throws NotEnoughBytesException если недостаточно байт для прочтения
     */
    protected Integer readFileNameSize(ByteBuf byteBuf) throws NotEnoughBytesException {

        if (this.fileNameSize != null) {
            return this.fileNameSize;
        }

        if (byteBuf.readableBytes() >= 4) {
            this.fileNameSize = byteBuf.readInt();
        } else {
            throw new NotEnoughBytesException("Недостаточно байт");
        }

        return this.fileNameSize;
    }

    /**
     * @param byteBuf буфер
     * @return название файла
     * @throws NotEnoughBytesException если недостаточно байт для прочтения
     */
    protected String readFileName(ByteBuf byteBuf) throws NotEnoughBytesException {
        if (this.fileName != null) {
            return this.fileName;
        }

        Integer size = this.readFileNameSize(byteBuf);

        byte[] fileName = new byte[size];

        if (byteBuf.readableBytes() >= size) {
            byteBuf.readBytes(fileName);
            this.fileName = new String(fileName);
        } else {
            throw new NotEnoughBytesException("Недостаточно байт");
        }

        return this.fileName;
    }

    /**
     * @param byteBuf буфер
     * @return размер файла
     * @throws NotEnoughBytesException если недостаточно байт для прочтения
     */
    protected long readFileSize(ByteBuf byteBuf) throws NotEnoughBytesException {

        if (this.fileSize != null) {
            return this.fileSize;
        }

        if (byteBuf.readableBytes() >= 8) {
            this.fileSize = byteBuf.readLong();
        } else {
            throw new NotEnoughBytesException("Недостаточно байт");
        }
        return this.fileSize;
    }

    /**
     *
     * @param fileName название файла
     * @return путь к файлу, куда нужно произвести запись, клиент и сервер переопределяет этот метод, у каждого будет свой каталог
     */
    public abstract Path getFilePath(String fileName);

    public void reset() {
        this.fileNameSize = null;
        this.fileName = null;
        this.fileSize = null;
        this.fileCreated = false;
    }


}
