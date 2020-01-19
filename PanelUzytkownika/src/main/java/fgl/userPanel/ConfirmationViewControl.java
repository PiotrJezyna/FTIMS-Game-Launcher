package fgl.userPanel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class ConfirmationViewControl {

    private AnchorPane root;

    @FXML
    public TextField confirmationTextField;

    @FXML
    public TextField usernameTextField;

    public void init(AnchorPane root) {
        this.root = root;
    }

    public void checkConfirmationString(ActionEvent actionEvent) throws IOException, SQLException {

        String userConfirmationNumber = UserSession.getUserSession().getCurrentUser().getConfirmationString();

        String inputString = confirmationTextField.getText();
        System.out.println(inputString);

        if (userConfirmationNumber.equals(inputString)) {
            informationWindow("Information", "Your account is confirmed");

            //*************************************
            //TODO change
            Parent root = FXMLLoader.load(getClass().getResource("/LoginView.fxml"));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            //window.initStyle(StageStyle.TRANSPARENT);
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
