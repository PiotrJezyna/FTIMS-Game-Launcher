import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class RegisterViewControl {
    @FXML
    private TextField name, surname, userName, email, password, repeatPassword;
    @FXML
    private Label label;

    public void openLoginScene(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("LoginView.fxml"));

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(scene);
        window.initStyle(StageStyle.TRANSPARENT);
        window.show();
    }

    public void registerUser(ActionEvent actionEvent) throws SQLException {
        String uName = name.getText();
        String uSurname = surname.getText();
        String uUserName = userName.getText();
        String uEmail = email.getText();


        if (uName.equals("") || uSurname.equals(null) || uUserName.equals(null) || uEmail.equals(null)) {
            warningWindow("Not all data was filled in", "Please fill in all data!" );
        } else {
            Registration registration = new Registration();
            registration.createUser(uUserName, uName, uSurname, uEmail);
            informationWindow("Successfully registered", "You have created Your account. Please Login!");
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
