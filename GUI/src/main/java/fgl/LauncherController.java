package fgl;

import fgl.admin.AdminPageController;
import javafx.fxml.FXML;
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void userButton() {

    }

    public void catalogButton() {

    }

    public void libraryButton() {
    }
}
