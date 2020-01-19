import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class ChangePasswordViewControl {

    @FXML
    public PasswordField passwordField;
    public PasswordField repeatPasswordField;

    private LoginViewControl login = new LoginViewControl();


    public void changePasswordAction(ActionEvent actionEvent) throws SQLException, IOException, NoSuchAlgorithmException {
        String password = passwordField.getText();
        String repeatPassword = repeatPasswordField.getText();
        UserDAO dao = new UserDAO();

        User user = login.getUserSession().getCurrentUser();
        if (!password.equals("") && !repeatPassword.equals("") && password.equals(repeatPassword)) {

            user.setPassword(encryptPassword(password));
            dao.update(user);
            informationWindow("Information", "Password changed successfully! Please login :)");

            Parent root = FXMLLoader.load(getClass().getResource("LoginView.fxml"));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.initStyle(StageStyle.TRANSPARENT);
            window.show();
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
