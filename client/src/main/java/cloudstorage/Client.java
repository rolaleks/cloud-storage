package cloudstorage;


import cloudstorage.net.FilePackage;

public class Client {

    public static void main(String[] args) {

        CloudClient cloudClient = new CloudClient();

        FilePackage filePackage = new FilePackage("test.file");
        cloudClient.send(filePackage);

    }

}
