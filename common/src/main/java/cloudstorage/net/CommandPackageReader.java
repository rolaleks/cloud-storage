package cloudstorage.net;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import io.netty.buffer.ByteBuf;

import java.io.BufferedInputStream;
import java.io.IOException;

abstract public class CommandPackageReader implements PackageReadable {

    private Integer commandNameSize;
    private String commandName;
    private Integer paramsSize;
    private String params;

    abstract public boolean read(ByteBuf byteBuf);

    protected Integer readCommandNameSize(ByteBuf byteBuf) throws NotEnoughBytesException {
        if (commandNameSize != null) {
            return commandNameSize;
        }
        if (byteBuf.readableBytes() >= 4) {
            commandNameSize = byteBuf.readInt();
        } else {
            throw new NotEnoughBytesException("Недостаточно байт");
        }

        return commandNameSize;
    }


    protected String readCommandName(ByteBuf byteBuf) throws NotEnoughBytesException {
        if (commandName != null) {
            return commandName;
        }
        Integer size = this.readCommandNameSize(byteBuf);

        byte[] fileName = new byte[size];
        if (byteBuf.readableBytes() >= size) {
            byteBuf.readBytes(fileName);
            this.commandName = new String(fileName);
        } else {
            throw new NotEnoughBytesException("Недостаточно байт");
        }

        return this.commandName;
    }


    protected int readParamsSize(ByteBuf byteBuf) throws NotEnoughBytesException {
        if (paramsSize != null) {
            return paramsSize;
        }
        if (byteBuf.readableBytes() >= 4) {
            paramsSize = byteBuf.readInt();
        } else {
            throw new NotEnoughBytesException("Недостаточно байт");
        }

        return paramsSize;
    }


    protected String readParams(ByteBuf byteBuf) throws NotEnoughBytesException {

        if (params != null) {
            return params;
        }
        Integer size = this.readParamsSize(byteBuf);

        byte[] paramsBytes = new byte[size];
        if (byteBuf.readableBytes() >= size) {
            byteBuf.readBytes(paramsBytes);
            params = new String(paramsBytes);
        } else {
            throw new NotEnoughBytesException("Недостаточно байт");
        }

        return params;
    }

    public void reset() {
        commandNameSize = null;
        commandName = null;
        paramsSize = null;
        params = null;
    }

}
