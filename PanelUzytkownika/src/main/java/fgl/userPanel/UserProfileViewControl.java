package fgl.userPanel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserProfileViewControl {


    public javafx.scene.control.TextField nameField;
    public javafx.scene.control.TextField surnameField;
    public javafx.scene.control.TextField usernameField;
    public javafx.scene.control.TextField emailField;
    public Button buttonEditProfile;

    private Login loggedIn = new Login();

    @FXML
    protected void initialize() {
        nameField.setText(getLoggedInUserName());
        surnameField.setText(getLoggedInUserSurname());
        usernameField.setText(getLoggedInUserUsername());
        emailField.setText(getLoggedInUserEmail());
    }

    private String getLoggedInUserName(){
        String name = loggedIn.getUserSession().getCurrentUser().getName();
        return name;
    }
    private String getLoggedInUserSurname(){
        String surname = loggedIn.getUserSession().getCurrentUser().getSurname();
        return surname;
    }
    private String getLoggedInUserUsername(){
        String username = loggedIn.getUserSession().getCurrentUser().getUsername();
        return username;
    }
    private String getLoggedInUserEmail(){
        String email = loggedIn.getUserSession().getCurrentUser().getEmail();
        return email;
    }

    public void goToEditProfile(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/EditUserProfileView.fxml"));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        //window.initStyle(StageStyle.TRANSPARENT);
        window.show();
    }

    public void logoutUser(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/LoginView.fxml"));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        //window.initStyle(StageStyle.TRANSPARENT);
        window.show();
    }
}
