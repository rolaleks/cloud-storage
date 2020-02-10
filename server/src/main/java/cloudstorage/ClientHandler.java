package cloudstorage;

import javax.security.auth.login.CredentialException;
import java.io.*;
import java.net.Socket;

public class ClientHandler {

    private CloudServer server;
    private Socket socket;
    private BufferedOutputStream out;
    private BufferedInputStream in;

    private boolean isAuth;

    public ClientHandler(CloudServer server, Socket socket) {
        try {
            this.socket = socket;
            this.server = server;
            this.in = new BufferedInputStream(socket.getInputStream());
            this.out = new BufferedOutputStream(socket.getOutputStream());
            new Thread(() -> {
                try {
                    PackageReader packageReader = new PackageReader(in, this);

                    //первый пакет должен быть с авторизацие, иначен ошибка
                    packageReader.read();

                    if (!this.isAuth) {
                        throw new CredentialException("Неверный логин или пароль");
                    }

                    while (true) {
                        packageReader.read();
                    }


                } catch (IOException | CredentialException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setAuth(boolean auth) {
        isAuth = auth;
    }
}
