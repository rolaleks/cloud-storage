package cloudstorage;

import cloudstorage.net.Package;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

public class CloudClient {

    private Socket socket;
    private BufferedOutputStream bufferedOutputStream;

    public CloudClient() {
        try {
            this.socket = new Socket("localhost", 8888);
            this.bufferedOutputStream = new BufferedOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        //TODO package receiver
    }

    public void send(Package pack) {

        try {
            byte[] bytes;
            while ((bytes = pack.getBytes()).length > 0) {
                this.bufferedOutputStream.write(bytes);
            }
            this.bufferedOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
