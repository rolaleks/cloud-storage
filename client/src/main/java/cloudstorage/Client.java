package cloudstorage;


import cloudstorage.net.CommandPackage;
import cloudstorage.net.FilePackage;
import cloudstorage.net.PackageCommandType;

public class Client {

    public static void main(String[] args) {

        CloudClient cloudClient = new CloudClient();


        CommandPackage authPackage = new CommandPackage(PackageCommandType.AUTH, "test:test");
        FilePackage filePackage = new FilePackage("test.file");

        cloudClient.send(authPackage);
        cloudClient.send(filePackage);

    }

}
