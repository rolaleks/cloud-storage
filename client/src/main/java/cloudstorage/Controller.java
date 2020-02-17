package cloudstorage;

import cloudstorage.net.CommandPackage;
import cloudstorage.net.PackageCommandType;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    HBox upperPanel;

    @FXML
    HBox filePanel;

    @FXML
    TextField loginField;

    @FXML
    PasswordField passwordField;

    @FXML
    ListView<String> clientsList;

    @FXML
    ListView<String> serverList;

    CloudClient cloudClient;

    private boolean isAuthorized;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setAuthorized(false);
    }

    public void setAuthorized(boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
        if (!isAuthorized) {
            upperPanel.setVisible(true);
            upperPanel.setManaged(true);
            filePanel.setVisible(false);
            filePanel.setManaged(false);
        } else {
            upperPanel.setVisible(false);
            upperPanel.setManaged(false);
            filePanel.setVisible(true);
            filePanel.setManaged(true);
            refreshFiles();
        }
    }

    public void refreshFiles() {
        Platform.runLater(() -> {
            this.clientsList.getItems().clear();
            this.clientsList.getItems().addAll(getCloudClient().getLocalFiles());
        });

        getCloudClient().getRemoteFiles();
    }

    public void setRemoteFiles(String[] files) {
        Platform.runLater(() -> {
            this.serverList.getItems().clear();
            this.serverList.getItems().addAll(files);
        });
    }

    public void tryToAuth() {

        getCloudClient().auth(loginField.getText(), passwordField.getText());
        loginField.clear();
        passwordField.clear();
    }

    private CloudClient getCloudClient() {
        if (cloudClient == null || cloudClient.getIsClosed()) {
            System.out.println("new cloud client");
            this.cloudClient = new CloudClient();
        }
        return this.cloudClient;
    }

    public void sendToServer(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            String file = clientsList.getSelectionModel().getSelectedItem();
            if (file != null)
                getCloudClient().sendFileToServer(file);

        }

    }

    public void sendToClient(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            String file = serverList.getSelectionModel().getSelectedItem();
            if (file != null)
                getCloudClient().loadFileFromServer(file);

        }
    }
}
