package cloudstorage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CloudServer {

    private ServerSocket serverSocket;

    public CloudServer() {
        try {
            this.serverSocket = new ServerSocket(8888);
            while (true) {
                Socket socket = this.serverSocket.accept();
                System.out.println("Новое подключение");
                new ClientHandler(this, socket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void close() {

    }
}
