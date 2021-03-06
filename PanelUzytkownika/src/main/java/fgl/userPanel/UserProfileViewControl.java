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
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class UserProfileViewControl {

    private AnchorPane root;
    private AnchorPane avatarSpace;

    @FXML
    public TextField nameField;

    @FXML
    public TextField surnameField;

    @FXML
    public TextField emailField;

    @FXML
    public Button buttonEditProfile;

    @FXML
    protected void initialize() {
        nameField.setText(getLoggedInUserName());
        surnameField.setText(getLoggedInUserSurname());
        emailField.setText(getLoggedInUserEmail());
    }

    public void init( AnchorPane root, AnchorPane avatarSpace ) {
        this.root = root;
        this.avatarSpace = avatarSpace;
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource( "/EditUserProfileView.fxml" ));
        AnchorPane pane = loader.load();
        EditUserProfileViewControl ctrl = loader.getController();
        ctrl.init( root, avatarSpace );

        root.getChildren().clear();
        root.getChildren().add( pane );
    }

    public void logoutUser(ActionEvent actionEvent) throws IOException {
        UserSession.getUserSession().setCurrentUser( null );

        File loginFile = new File(System.getProperty( "user.dir" ) + "\\credentials\\login.txt");
        File passwordFile = new File(System.getProperty( "user.dir" ) + "\\credentials\\password.txt");

        if ( loginFile.exists() ) loginFile.delete();
        if ( passwordFile.exists() ) passwordFile.delete();

        Parent root = FXMLLoader.load(getClass().getResource("/Launcher.fxml"));
        Scene scene = new Scene(root);
        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        window.setTitle("FTIMS Game Launcher");
        window.setResizable(false);
        window.setScene(scene);
        window.show();
    }
}
