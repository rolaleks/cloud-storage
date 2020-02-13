package cloudstorage.net;

import io.netty.buffer.ByteBuf;

import java.io.*;

abstract public class FilePackageReader implements PackageReadable {

    protected Integer fileNameSize;
    protected String fileName;
    protected Long fileSize;

    abstract public boolean read(ByteBuf byteBuf);

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


}
