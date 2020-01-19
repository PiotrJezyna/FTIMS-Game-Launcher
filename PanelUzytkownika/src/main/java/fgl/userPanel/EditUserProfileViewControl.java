package fgl.userPanel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class EditUserProfileViewControl {

    private AnchorPane root;

    @FXML
    public TextField nameEditField;

    @FXML
    public TextField surnameEditField;

    @FXML
    public TextField usernameEditField;

    @FXML
    public TextField emailEditField;

    @FXML
    public Button buttonSaveChanges;

    @FXML
    protected void initialize() {
        nameEditField.setText(getLoggedInUserName());
        surnameEditField.setText(getLoggedInUserSurname());
        usernameEditField.setText(getLoggedInUserUsername());
        emailEditField.setText(getLoggedInUserEmail());
    }

    public void init(AnchorPane root) {
        this.root = root;
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

    public void updateUserData(javafx.event.ActionEvent actionEvent) throws IOException, SQLException {
        UserDAO dao = new UserDAO();

        String name = nameEditField.getText();
        String surname = surnameEditField.getText();
        String username = usernameEditField.getText();
        String email = emailEditField.getText();

        User user = UserSession.getUserSession().getCurrentUser();
        //user = new User(name, surname, username, email);
        user.setName(name);
        user.setSurname(surname);
        user.setUsername(username);
        user.setEmail(email);
        if (checkForMailAndUsername(username, email)) {
            dao.update(user);

            backToUserProfile(null);
        } else {
            warningWindow("Warning", "User with this email/username already exists");
        }
    }

    public void backToUserProfile(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource( "/UserProfileView.fxml" ));
        AnchorPane pane = loader.load();
        UserProfileViewControl ctrl = loader.getController();
        ctrl.init( root );

        root.getChildren().clear();
        root.getChildren().add( pane );
    }

    private boolean checkForMailAndUsername(String uUserName, String uEmail) throws SQLException {
        UserDAO dao = new UserDAO();
        List<User> allUsers = dao.getAll();
        boolean flag = true;

        for (int i = 0; i < allUsers.size(); i++) {
            if (uUserName.equals(allUsers.get(i).getUsername())) {
                flag = false;
            } else if (uEmail.equals(allUsers.get(i).getEmail())) {
                flag = false;
            }
        }
        return flag;
    }

    private void warningWindow(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
