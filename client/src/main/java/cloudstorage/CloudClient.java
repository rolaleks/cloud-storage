package cloudstorage;

import cloudstorage.helpers.FolderReader;
import cloudstorage.net.CommandPackage;
import cloudstorage.net.FilePackage;
import cloudstorage.net.Package;
import cloudstorage.net.PackageCommandType;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import javafx.scene.shape.Path;


public class CloudClient {

    public static final String localPath = "localStorage";

    private ServerHandler serverHandler;

    private boolean isClosed;
    private boolean isAuth;

    public CloudClient() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        CloudClient client = this;
        isClosed = false;
        new Thread(() -> {
            try {

                Bootstrap b = new Bootstrap();
                b.group(workerGroup);
                b.channel(NioSocketChannel.class);
                b.option(ChannelOption.SO_KEEPALIVE, true);
                b.handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        serverHandler = new ServerHandler(client, ch);
                        ch.pipeline().addLast(serverHandler);
                    }
                });
                ChannelFuture f = b.connect("localhost", 8888).sync();
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
                this.isClosed = true;
            }
        }).start();
    }

    public void sendPackage(Package p) {

        //Ждем подключение к серверу
        int tryCount = 0;
        while (this.serverHandler == null && !isClosed) {
            tryCount++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (tryCount == 20) {
                break;
            }
        }
        if (this.serverHandler == null) {
            throw new RuntimeException("Невозможно подключиться к серверу");
        }


        this.serverHandler.send(p);
    }

    public boolean getIsClosed() {

        return isClosed;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
        System.out.println("setAuth");
        ControllerManager.getMainController().setAuthorized(auth);
    }

    public void auth(String login, String pass) {
        CommandPackage authPackage = new CommandPackage(PackageCommandType.AUTH, login + ":" + pass);
        sendPackage(authPackage);
    }

    public String[] getLocalFiles() {
        FolderReader folderReader = new FolderReader(localPath);
        return folderReader.getFileNames();
    }

    public void getRemoteFiles() {
        CommandPackage authPackage = new CommandPackage(PackageCommandType.GET_FILES, "");
        sendPackage(authPackage);
    }

    public void sendFileToServer(String file) {
        FilePackage filePackage = new FilePackage(localPath + "/" + file);
        System.out.println(localPath + "/" + file);
        sendPackage(filePackage);
        getRemoteFiles();
    }

    public void loadFileFromServer(String file) {
        CommandPackage authPackage = new CommandPackage(PackageCommandType.GET_FILE, file);
        sendPackage(authPackage);
    }
}
