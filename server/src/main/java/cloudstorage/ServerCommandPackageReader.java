package cloudstorage;

import cloudstorage.net.CommandPackageReader;
import cloudstorage.net.CommandPerformable;
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

            /*
             * получаем название команды и ее параметры, если недостаточно байт получено, то будет брошено NotEnoughBytesException
             */
            String name = this.readCommandName(byteBuf);
            String params = this.readParams(byteBuf);

            //Определяем тип команды, и получаем соответсвенный обработчик для этой команды
            CommandPerformable command = client.getServerCommandDispatcher().getCommand(PackageCommandType.valueOf(name));
            command.perform(params);

            return true;

        } catch (NotEnoughBytesException e) {
            return false;
        }
    }


}
