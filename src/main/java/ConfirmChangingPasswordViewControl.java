import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;

public class ConfirmChangingPasswordViewControl {

    public TextField confirmationTextField;
    private LoginViewControl login = new LoginViewControl();

    public void checkConfirmationString(ActionEvent actionEvent) throws IOException, SQLException {

        String userConfirmationNumber = login.getUserSession().getCurrentUser().getConfirmationString();

        String inputString = confirmationTextField.getText();


        if (userConfirmationNumber.equals(inputString)) {
            informationWindow("Information", "You can now change your password");

            Parent root = FXMLLoader.load(getClass().getResource("ChangePasswordView.fxml"));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.initStyle(StageStyle.TRANSPARENT);
            window.show();
        } else {
            informationWindow("Information", "Wrong confirmation code");
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
