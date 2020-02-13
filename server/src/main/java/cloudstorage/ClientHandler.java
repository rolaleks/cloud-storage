package cloudstorage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.SocketChannel;

import javax.security.auth.login.CredentialException;
import java.io.*;

public class ClientHandler extends ChannelInboundHandlerAdapter {

    private CloudServer server;
    private SocketChannel socket;
    private PackageReader packageReader;

    private boolean isAuth;

    public ClientHandler(CloudServer server, SocketChannel socket) {
        this.socket = socket;
        this.server = server;
        this.packageReader = new PackageReader(this);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf input = (ByteBuf) msg;

        try {
            while (input.readableBytes() > 0) {
                System.out.println("READ");
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
    }
}
