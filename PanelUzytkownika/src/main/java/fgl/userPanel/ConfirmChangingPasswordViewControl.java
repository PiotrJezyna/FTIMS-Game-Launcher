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

public class ConfirmChangingPasswordViewControl {

    private AnchorPane root;
    private AnchorPane menu;

    @FXML
    private TextField confirmationTextField;

    public void init( AnchorPane root, AnchorPane menu ) {
        this.root = root;
        this.menu = menu;
    }

    public void checkConfirmationString(ActionEvent actionEvent) throws IOException, SQLException {

        String userConfirmationNumber = UserSession.getUserSession().getCurrentUser().getConfirmationString();

        String inputString = confirmationTextField.getText();

        if (userConfirmationNumber.equals(inputString)) {
            informationWindow("Information", "You can now change your password");

            FXMLLoader loader = new FXMLLoader(getClass().getResource( "/ChangePasswordView.fxml" ));
            AnchorPane pane = loader.load();
            ChangePasswordViewControl ctrl = loader.getController();
            ctrl.init( root, menu );

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
