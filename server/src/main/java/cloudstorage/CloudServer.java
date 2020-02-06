package cloudstorage;

import com.google.common.primitives.Ints;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CloudServer {

    private ServerSocket serverSocket;

    public CloudServer() {
        try {
            this.serverSocket = new ServerSocket(8888);
            Socket socket = this.serverSocket.accept();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());

            PackageReader packageReader = new PackageReader(bufferedInputStream);
            packageReader.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void close() {

    }
}
