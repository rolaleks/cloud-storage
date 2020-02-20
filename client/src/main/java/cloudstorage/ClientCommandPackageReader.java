package cloudstorage;

import cloudstorage.net.CommandPackageReader;
import cloudstorage.net.CommandPerformable;
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

            CommandPerformable command = serverHandler.getClientCommandDispatcher().getCommand(PackageCommandType.valueOf(name));
            command.perform(params);

            return true;

        } catch (NotEnoughBytesException e) {
            return false;
        }
    }


}
