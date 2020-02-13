package cloudstorage.net;

import io.netty.buffer.ByteBuf;

public interface PackageReadable {

    abstract public boolean read(ByteBuf byteBuf);
}
