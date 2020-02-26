package cloudstorage.net;

import io.netty.buffer.ByteBuf;

abstract public class CommandPackageReader implements PackageReadable {

    private Integer commandNameSize;
    private String commandName;
    private Integer paramsSize;
    private String params;

    abstract public boolean read(ByteBuf byteBuf);

    /**
     * @param byteBuf буфер
     * @return размер названия команды
     * @throws NotEnoughBytesException если недостаточно байт для прочтения
     */
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


    /**
     * @param byteBuf буфер
     * @return Название команбы
     * @throws NotEnoughBytesException если недостаточно байт для прочтения
     */
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


    /**
     * @param byteBuf буфер
     * @return размер параметров команды
     * @throws NotEnoughBytesException если недостаточно байт для прочтения
     */
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

    /**
     * @param byteBuf буфер
     * @return параметры команды
     * @throws NotEnoughBytesException если недостаточно байт для прочтения
     */
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
