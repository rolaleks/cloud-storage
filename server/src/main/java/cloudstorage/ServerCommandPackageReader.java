package cloudstorage;

import cloudstorage.commands.Auth;
import cloudstorage.net.CommandPackageReader;
import cloudstorage.net.NotEnoughBytesException;
import cloudstorage.net.PackageCommandType;
import io.netty.buffer.ByteBuf;

public class ServerCommandPackageReader extends CommandPackageReader {

    protected ClientHandler client;

    public ServerCommandPackageReader(ClientHandler client) {
        this.client = client;
    }

    public boolean read(ByteBuf byteBuf) {

        try {

            String name = this.readCommandName(byteBuf);
            String params = this.readParams(byteBuf);
            switch (PackageCommandType.valueOf(name)) {
                case AUTH:
                    (new Auth(params, this.client)).perform();
                    break;
                default:
                    throw new IllegalStateException("Unknown command: " + name);
            }
            return true;

        } catch (NotEnoughBytesException e) {
            return false;
        }
    }


}
