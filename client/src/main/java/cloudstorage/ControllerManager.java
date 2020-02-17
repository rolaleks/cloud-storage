package cloudstorage;

public class ControllerManager {

    private static Controller controller;

    public static Controller getMainController() {
        return controller;
    }

    public static void setMainController(Controller c) {
        controller = c;
    }

}
