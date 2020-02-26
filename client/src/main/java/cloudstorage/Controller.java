package cloudstorage;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    HBox upperPanel;

    @FXML
    VBox filePanel;

    @FXML
    TextField loginField;

    @FXML
    PasswordField passwordField;

    @FXML
    ListView<String> clientsList;

    @FXML
    ListView<String> serverList;

    @FXML
    TextField folder;

    CloudClient cloudClient;

    private boolean isAuthorized;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setAuthorized(false);
        initFolder();
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

    private void initFolder() {
        File f = new File(CloudClient.localPath);
        folder.setText(f.getAbsolutePath());
    }

    public String getFolder() {

        return folder.getText();
    }

    public void sendToClient(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            String file = serverList.getSelectionModel().getSelectedItem();
            if (file != null)
                getCloudClient().loadFileFromServer(file);

        }
    }

    /**
     * Смена локальной папки с файлами
     * @param actionEvent
     */
    public void folderSelect(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(folder.getScene().getWindow());

        if (selectedDirectory != null) {
            folder.setText(selectedDirectory.getAbsolutePath());
            refreshFiles();
        }
    }

    public void exit(ActionEvent actionEvent) {
        Platform.exit();
    }
}
