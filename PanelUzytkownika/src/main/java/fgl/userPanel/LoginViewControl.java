package fgl.userPanel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;

public class LoginViewControl {

    @FXML
    private AnchorPane root;

    @FXML
    private TextField name, username;

    String uName, uUsername;

    public void openRegisterScene(ActionEvent actionEvent) throws IOException {
        AnchorPane pain = FXMLLoader.load(getClass().getResource("/RegisterView.fxml"));

        root.getChildren().clear();
        root.getChildren().add(pain);
    }

    public void loginButtonClicked(ActionEvent actionEvent) throws SQLException {
        uName = name.getText();
        uUsername = username.getText();
        Login login = new Login();
        if(login.validate(uName, uUsername)){
            String yourname = login.getUserSession().getCurrentUser().getName();
            informationWindow("Successfully logged in", "You have logged in successfully");
            root.setDisable(true);
            root.setVisible(false);
        } else {
            informationWindow("Wrong credentials", "Please write your email and username");
        }
    }

    private void informationWindow(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
