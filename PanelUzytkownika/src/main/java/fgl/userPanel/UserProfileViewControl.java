package fgl.userPanel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class UserProfileViewControl {

    @FXML
    private AnchorPane root;

    @FXML
    public TextField nameField;

    @FXML
    public TextField surnameField;

    @FXML
    public TextField usernameField;

    @FXML
    public TextField emailField;

    @FXML
    public Button buttonEditProfile;

    @FXML
    protected void initialize() {
        nameField.setText(getLoggedInUserName());
        surnameField.setText(getLoggedInUserSurname());
        usernameField.setText(getLoggedInUserUsername());
        emailField.setText(getLoggedInUserEmail());
    }

    private String getLoggedInUserName(){
        String name = UserSession.getUserSession().getCurrentUser().getName();
        return name;
    }
    private String getLoggedInUserSurname(){
        String surname = UserSession.getUserSession().getCurrentUser().getSurname();
        return surname;
    }
    private String getLoggedInUserUsername(){
        String username = UserSession.getUserSession().getCurrentUser().getUsername();
        return username;
    }
    private String getLoggedInUserEmail(){
        String email = UserSession.getUserSession().getCurrentUser().getEmail();
        return email;
    }

    public void goToEditProfile(ActionEvent actionEvent) throws IOException {
        AnchorPane pain = FXMLLoader.load( getClass().getResource( "/EditUserProfileView.fxml" ) );

        root.getChildren().clear();
        root.getChildren().add( pain );
        /*
        Parent root = FXMLLoader.load(getClass().getResource("/EditUserProfileView.fxml"));
        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        window.setScene(scene);
        //window.initStyle(StageStyle.TRANSPARENT);
        window.show();*/
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
