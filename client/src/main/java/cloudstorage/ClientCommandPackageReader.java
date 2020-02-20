package cloudstorage;

import cloudstorage.commands.AuthStatus;
import cloudstorage.commands.GetFiles;
import cloudstorage.net.CommandPackageReader;
import cloudstorage.net.NotEnoughBytesException;
import cloudstorage.net.PackageCommandType;
import io.netty.buffer.ByteBuf;

public class ClientCommandPackageReader extends CommandPackageReader {

    protected ServerHandler serverHandler;

    public ClientCommandPackageReader(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    public boolean read(ByteBuf byteBuf) {

        try {

            String name = this.readCommandName(byteBuf);
            String params = this.readParams(byteBuf);
            switch (PackageCommandType.valueOf(name)) {
                case AUTH:
                    (new AuthStatus(params, this.serverHandler)).perform();
                    break;
                case GET_FILES:
                    (new GetFiles(params, this.serverHandler)).perform();
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
