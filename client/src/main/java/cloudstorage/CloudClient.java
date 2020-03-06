package cloudstorage;

import cloudstorage.helpers.FolderReader;
import cloudstorage.net.*;
import cloudstorage.net.Package;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


public class CloudClient {

    /**
     * путь локального хранилища по умолчанию
     */
    public static final String localPath = "localStorage";

    private ServerHandler serverHandler;

    private boolean isClosed;
    private boolean isAuth;
    private Thread clientThread;

    public CloudClient() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        CloudClient client = this;
        isClosed = false;
        clientThread = new Thread(() -> {
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
                        synchronized (client) {
                            client.notify();
                        }
                    }
                });
                ChannelFuture f = b.connect("localhost", 8888).sync();
                f.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                workerGroup.shutdownGracefully();
                serverHandler = null;
                this.isClosed = true;
            }
        });
        clientThread.start();

        //ждем подключение клиента к серверу
        if (serverHandler == null) {
            try {
                synchronized (client) {
                    client.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Отправка всех запросов/пакетов на сервер
     *
     * @param p Пакет
     */
    public void sendPackage(Package p) {

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
        ControllerManager.getMainController().setAuthorized(auth);
    }

    public void auth(String login, String pass) {
        CommandPackage authPackage = new CommandPackage(PackageCommandType.AUTH, login + ":" + pass);
        sendPackage(authPackage);
    }

    /**
     * Получение списка локальных файлов
     */
    public String[] getLocalFiles() {
        FolderReader folderReader = new FolderReader(ControllerManager.getMainController().getFolder());
        return folderReader.getFileNames();
    }

    /**
     * Получение списка файлов на сервере
     */
    public void getRemoteFiles() {
        CommandPackage authPackage = new CommandPackage(PackageCommandType.GET_FILES, "");
        sendPackage(authPackage);
    }

    /**
     * Отправка файла на сервер по имени файла
     *
     * @param file имя файла
     */
    public void sendFileToServer(String file) {
        FilePackage filePackage = new FilePackage(ControllerManager.getMainController().getFolder() + "/" + file);
        filePackage.setPartitionSendHandler(new PackageHandler(filePackage) {
            @Override
            public Void call()  {
                ControllerManager.getMainController().setPercent(filePackage.getPercent());
                return null;
            }
        });

        sendPackage(filePackage);
        getRemoteFiles();
    }

    /**
     * Загрузка файла с сервер на клиент по имени файла
     *
     * @param file имя файла
     */
    public void loadFileFromServer(String file) {
        CommandPackage authPackage = new CommandPackage(PackageCommandType.GET_FILE, file);
        sendPackage(authPackage);
    }
}
