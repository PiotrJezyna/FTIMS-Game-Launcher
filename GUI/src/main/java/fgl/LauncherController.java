package fgl;

import fgl.admin.AdminPageController;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

public class LauncherController {

    private AdminPageController adminPageController;

    private AnchorPane loadedFxml;

    @FXML
    private AnchorPane paneChanger;

    public LauncherController() {
        adminPageController = new AdminPageController();
    }

    public void adminButton() {
        try {

            adminPageController.start(paneChanger);

        } catch (AccessDeniedException e) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("You are not prepared to enter this page!");
            alert.showAndWait();

        } catch (IOException e) {

            e.printStackTrace();

        }
    }

    public void userButton() {

    }

    public void catalogButton() {
        try {
            loadedFxml = FXMLLoader.load( getClass().getResource("/CatalogCard.fxml") );
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        paneChanger.getChildren().clear();
        paneChanger.getChildren().add(loadedFxml);
    }

    public void libraryButton() {
    }
}
