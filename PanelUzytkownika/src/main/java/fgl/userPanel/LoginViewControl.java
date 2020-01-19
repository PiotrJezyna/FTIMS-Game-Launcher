package fgl.userPanel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class LoginViewControl {

    private AnchorPane root;

    @FXML
    private TextField username, password;

    private String uUsername, uPassword;

    public void init(AnchorPane root) {
        this.root = root;
    }

    public void openRegisterScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource( "/RegisterView.fxml" ));
        AnchorPane pane = loader.load();
        RegisterViewControl ctrl = loader.getController();
        ctrl.init( root );

        root.getChildren().clear();
        root.getChildren().add( pane );
    }

    public void loginButtonClicked(ActionEvent actionEvent) throws SQLException, IOException, NoSuchAlgorithmException {
        uUsername = username.getText();
        uPassword =password.getText();
        uPassword = encryptPassword(password.getText());

        Login login = new Login();
        if( login.validate( uUsername, uPassword ) ) {
            Text text = new Text();
            text.setX( 70 );
            text.setY( 50 );
            text.setFont( Font.font( "Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 15 ) );
            text.setText( "You have logged in successfully" );

            root.getChildren().clear();
            root.getChildren().add( text );
        } else {
            warningWindow( "Warning", "Wrong credentials. Please try again" );
        }
    }

    private void warningWindow(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.showAndWait();
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
}
