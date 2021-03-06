package fgl.userPanel;

import fgl.communication.MailHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class RegisterViewControl {

    private AnchorPane root;
    private AnchorPane avatarSpace;

    @FXML
    private TextField name, surname, userName, email, password, repeatPassword;

    @FXML
    private Label label;

    public void init( AnchorPane root, AnchorPane avatarSpace ) {
        this.root = root;
        this.avatarSpace = avatarSpace;
    }

    public void openLoginScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource( "/LoginView.fxml" ));
        AnchorPane pane = loader.load();
        LoginViewControl ctrl = loader.getController();
        ctrl.init( root, avatarSpace );

        double width = pane.getPrefWidth();
        pane.setLayoutX( (root.getPrefWidth() - width) / 2 );

        root.getChildren().clear();
        root.getChildren().add( pane );
    }

    public void registerUser(ActionEvent actionEvent) throws SQLException, NoSuchAlgorithmException, IOException {
        String uName = name.getText();
        String uSurname = surname.getText();
        String uUserName = userName.getText();
        String uEmail = email.getText();
        String uPassword = password.getText();
        String uRepeatPassword = repeatPassword.getText();

        if (uName.equals("") || uSurname.equals("") || uUserName.equals("") || uEmail.equals("") || uPassword.equals("") || uRepeatPassword.equals("")) {
            warningWindow("Not all data was filled in", "Please fill in all data!" );
        } else if (!uPassword.equals(uRepeatPassword)) {
            warningWindow("Wrong password", "Repeated password does not match" );
        }
        else {
            Registration registration = new Registration();
            if (registration.createUser(uUserName, uName, uSurname, uEmail, uPassword)) {
                informationWindow("Successfully registered", "Please confirm your account");

                FXMLLoader loader = new FXMLLoader(getClass().getResource( "/ConfirmationView.fxml" ));
                AnchorPane pane = loader.load();
                ConfirmationViewControl ctrl = loader.getController();
                ctrl.init( root, avatarSpace );

                double width = pane.getPrefWidth();
                pane.setLayoutX( (root.getPrefWidth() - width) / 2 );

                root.getChildren().clear();
                root.getChildren().add( pane );
            } else
                warningWindow("Warning", "User with this email/username already exists" );
            }
    }


    private void warningWindow(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void informationWindow(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }


}
