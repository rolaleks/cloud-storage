package cloudstorage.net;

import java.util.concurrent.Callable;

public abstract class PackageHandler implements Callable<Void> {

    protected FilePackage filePackage;

    public PackageHandler(FilePackage filePackage) {
        this.filePackage = filePackage;
    }
}
