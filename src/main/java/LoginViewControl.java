import com.mysql.cj.log.Log;
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
import java.sql.SQLException;

public class LoginViewControl {
    @FXML
    private TextField name, surname;

    String uName, uSurname;




    public void openRegisterScene(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("RegisterView.fxml"));

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(scene);
        window.initStyle(StageStyle.TRANSPARENT);
        window.show();
    }

    public void loginButtonClicked(ActionEvent actionEvent) throws SQLException {
        uName = name.getText();
        uSurname = surname.getText();
        Login login = new Login();
        if(login.validate(uName, uSurname)){
            String yourname = login.getUserSession().getCurrentUser().getName();
            informationWindow("Successfully logged in", "You have logged in successfully \n Your name: " + yourname);

        } else {
            informationWindow("Wrong credentials", "Please write your email and surname");
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
