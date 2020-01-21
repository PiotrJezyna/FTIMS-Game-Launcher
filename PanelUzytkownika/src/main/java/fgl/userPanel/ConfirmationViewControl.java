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
    private AnchorPane avatarSpace;

    @FXML
    public TextField confirmationTextField;

    @FXML
    public TextField usernameTextField;

    public void init(AnchorPane root, AnchorPane avatarSpace) {
        this.root = root;
        this.avatarSpace = avatarSpace;
    }

    public void checkConfirmationString(ActionEvent actionEvent) throws IOException, SQLException {

        String userConfirmationNumber = UserSession.getUserSession().getConfirmationCode();

        String inputString = confirmationTextField.getText();
        System.out.println(inputString);

        if (userConfirmationNumber.equals(inputString)) {
            informationWindow("Information", "Your account is confirmed");

            FXMLLoader loader = new FXMLLoader(getClass().getResource( "/LoginView.fxml" ));
            AnchorPane pane = loader.load();
            LoginViewControl ctrl = loader.getController();
            ctrl.init( root, avatarSpace );

            double width = pane.getPrefWidth();
            pane.setLayoutX( (root.getPrefWidth() - width) / 2 );

            root.getChildren().clear();
            root.getChildren().add( pane );
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
