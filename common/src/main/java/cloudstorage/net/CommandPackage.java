package cloudstorage.net;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;

public class CommandPackage extends Package {

    public final static byte flag = 11;

    private PackageCommandType command;
    private String params;
    private boolean isSent;

    public CommandPackage(PackageCommandType command, String params) {
        this.command = command;
        this.params = params;
    }

    @Override
    byte[] getBodyBytes() {

        if (!this.isSent) {
            this.isSent = true;
            return params.getBytes();
        }

        return new byte[0];
    }

    @Override
    byte[] getHeadBytes() {

        byte[] commandSizeBytes = Ints.toByteArray(this.command.toString().length());
        byte[] commandBytes = this.command.toString().getBytes();
        byte[] sizeBytes = Ints.toByteArray(this.params.getBytes().length);


        return Bytes.concat(commandSizeBytes, commandBytes, sizeBytes);
    }

    @Override
    byte getFlag() {
        return flag;
    }
}
