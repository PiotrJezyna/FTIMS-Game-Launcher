package fgl.userPanel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class ChangePasswordViewControl {

    private AnchorPane root;
    private AnchorPane avatarSpace;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField repeatPasswordField;

    public void init( AnchorPane root, AnchorPane avatarSpace ) {
        this.root = root;
        this.avatarSpace = avatarSpace;
    }

    public void changePasswordAction(ActionEvent actionEvent) throws SQLException, IOException, NoSuchAlgorithmException {
        String password = passwordField.getText();
        String repeatPassword = repeatPasswordField.getText();
        UserDAO dao = new UserDAO();

        User user = UserSession.getUserSession().getCurrentUser();
        if (!password.equals("") && !repeatPassword.equals("") && password.equals(repeatPassword)) {

            user.setPassword(encryptPassword(password));
            dao.update(user);
            informationWindow("Information", "Password changed successfully! Please login :)");

            File loginFile = new File(System.getProperty( "user.dir" ) + "\\credentials\\login.txt");
            File passwordFile = new File(System.getProperty( "user.dir" ) + "\\credentials\\password.txt");

            if ( loginFile.exists() ) loginFile.delete();
            if ( passwordFile.exists() ) passwordFile.delete();

            FXMLLoader loader = new FXMLLoader(getClass().getResource( "/LoginView.fxml" ));
            AnchorPane pane = loader.load();
            LoginViewControl ctrl = loader.getController();
            ctrl.init( root, avatarSpace );

            double width = pane.getPrefWidth();
            pane.setLayoutX( (root.getPrefWidth() - width) / 2 );

            root.getChildren().clear();
            root.getChildren().add( pane );
        }
    }

    public String encryptPassword(String plainPassword) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(
                plainPassword.getBytes(StandardCharsets.UTF_8));

        String codedPassword = bytesToHex(encodedhash);

        return codedPassword;
    }

    private static String bytesToHex(byte[] hash) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private void informationWindow(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
