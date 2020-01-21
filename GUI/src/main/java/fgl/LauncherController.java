package fgl;

import fgl.admin.AdminPageController;
import fgl.userPanel.LoginViewControl;
import fgl.userPanel.UserProfileViewControl;
import fgl.userPanel.UserSession;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

public class LauncherController {

    private AdminPageController adminPageController;
    private UserSession userSession;

    private AnchorPane loadedFxml;

    @FXML
    private AnchorPane avatarSpace;

    @FXML
    private AnchorPane paneChanger;

    @FXML
    private AnchorPane loginPanel;

    public LauncherController() {
        adminPageController = new AdminPageController();
    }

    public void initialize() {
        boolean load = false;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource( "/LoginView.fxml" ));
            loadedFxml = loader.load();
            LoginViewControl ctrl = loader.getController();
            load = ctrl.init( loginPanel, avatarSpace );
        } catch ( IOException e ) {
            e.printStackTrace();
        }

        if ( !load ) {
            double width = loadedFxml.getPrefWidth();
            loadedFxml.setLayoutX( (loginPanel.getPrefWidth() - width) / 2 );

            loginPanel.getChildren().clear();
            loginPanel.getChildren().add( loadedFxml );
        }

        userSession = UserSession.getUserSession();
    }

    public void adminButton() {
        if ( userSession.getCurrentUser() == null ) {
            showAlert( "Information", "You have to login first!" );
        } else {

            try {
                adminPageController.start( paneChanger, userSession );
            } catch ( AccessDeniedException e ) {
                showAlert( "Warning", "You are not prepared to enter this page!" );
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }

    public void userButton() {
        if ( userSession.getCurrentUser() == null ) {
            showAlert( "Information", "You have to login first!" );
        } else {

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource( "/UserProfileView.fxml" ));
                loadedFxml = loader.load();
                UserProfileViewControl ctrl = loader.getController();
                ctrl.init( paneChanger, avatarSpace );
            } catch ( IOException e ) {
                e.printStackTrace();
            }

            paneChanger.getChildren().clear();
            paneChanger.getChildren().add( loadedFxml );
        }
    }

    public void catalogButton() {
        if ( userSession.getCurrentUser() == null ) {
            showAlert("Information", "You have to login first!");
        } else {
            try {
                loadedFxml = FXMLLoader.load( getClass().getResource("/CatalogCard.fxml") );
            } catch ( IOException e ) {
                e.printStackTrace();
            }

            paneChanger.getChildren().clear();
            paneChanger.getChildren().add( loadedFxml );
        }

    }

    public void libraryButton() {
        if ( userSession.getCurrentUser() == null ) {
            showAlert("Information", "You have to login first!");
        } else {
            try {
                loadedFxml = FXMLLoader.load( getClass().getResource("/LibraryCard.fxml") );
            } catch ( IOException e ) {
                e.printStackTrace();
            }

            paneChanger.getChildren().clear();
            paneChanger.getChildren().add( loadedFxml );
        }
    }

    public void newProductCardButton() {
        if ( userSession.getCurrentUser() == null ) {
            showAlert("Information", "You have to login first!");
        } else {
            try {
                loadedFxml = FXMLLoader.load( getClass().getResource("/NewProductCard.fxml") );
            } catch ( IOException e ) {
                e.printStackTrace();
            }

            paneChanger.getChildren().clear();
            paneChanger.getChildren().add( loadedFxml );
        }
    }

    private void showAlert( String title, String content ) {
        Alert alert = new Alert( Alert.AlertType.WARNING );
        alert.setTitle( title );
        alert.setHeaderText( null );
        alert.setContentText( content );
        alert.showAndWait();
    }
}
