package cloudstorage;

import cloudstorage.commands.ServerCommandDispatcher;
import cloudstorage.db.User;
import cloudstorage.net.CommandPackage;
import cloudstorage.net.Package;
import cloudstorage.net.PackageCommandType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

import javax.security.auth.login.CredentialException;
import java.io.*;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private CloudServer server;
    private SocketChannel socket;
    private PackageReader packageReader;
    private ServerCommandDispatcher serverCommandDispatcher;
    ChannelHandlerContext ctx;

    private boolean isAuth;
    private User user;

    public ClientHandler(CloudServer server, SocketChannel socket) {
        this.socket = socket;
        this.server = server;
        this.packageReader = new PackageReader(this);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        this.ctx = ctx;
        ByteBuf input = (ByteBuf) msg;

        try {
            while (input.readableBytes() > 0) {
                packageReader.read(input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    public void setAuth(boolean auth) {
        isAuth = auth;

        CommandPackage authStatus = new CommandPackage(PackageCommandType.AUTH, auth ? "1" : "0");
        this.send(authStatus);
    }

    public void send(Package pack) {
        if (ctx != null) {
            byte[] bytes;
            while ((bytes = pack.getBytes()).length > 0) {
                ctx.write(Unpooled.copiedBuffer(bytes));
                ctx.flush();
            }
        }
    }

    public void setUser(User user) {
        this.user = user;
    }


    public User getUser() {
        return this.user;
    }

    public ServerCommandDispatcher getServerCommandDispatcher() {
        if (this.serverCommandDispatcher == null) {
            this.serverCommandDispatcher = new ServerCommandDispatcher(this);
        }
        return this.serverCommandDispatcher;
    }
}
