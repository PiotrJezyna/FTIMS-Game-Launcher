package fgl.userPanel;

import fgl.drive.DriveDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class LoginViewControl {

    private AnchorPane root;
    private AnchorPane menu;

    @FXML
    private TextField username, password;

    private String uUsername, uPassword;

    public void init( AnchorPane root, AnchorPane menu ) {
        this.root = root;
        this.menu = menu;
    }

    public void openRegisterScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource( "/RegisterView.fxml" ));
        AnchorPane pane = loader.load();
        RegisterViewControl ctrl = loader.getController();
        ctrl.init( root, menu );

        double width = pane.getPrefWidth();
        pane.setLayoutX( (root.getPrefWidth() - width) / 2 );

        root.getChildren().clear();
        root.getChildren().add( pane );
    }

    public void loginButtonClicked(ActionEvent actionEvent) throws SQLException, IOException, NoSuchAlgorithmException {
        uUsername = username.getText();
        uPassword = password.getText();
        uPassword = encryptPassword(password.getText());

        Login login = new Login();
        if( login.validate( uUsername, uPassword ) ) {
            DriveDao driveDao = new DriveDao();
            User user = UserSession.getUserSession().getCurrentUser();
            Image image = new Image( driveDao.downloadAvatar( user.getUsername() )
                    .toURI().toString(), 120, 120, true, false );
            Circle clip = new Circle(menu.getPrefWidth() / 2, 80, 52);
            ImageView avatar = new ImageView(image);
            avatar.setX(33);
            avatar.setY(20);
            avatar.setClip(clip);

            Text username = new Text();
            username.setFont( Font.font( "Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 18 ) );
            username.setText( uUsername );
            username.setY( 160 );
            double width = username.getLayoutBounds().getWidth();
            username.setX( (menu.getPrefWidth() - width) / 2 );

            menu.getChildren().add( avatar );
            menu.getChildren().add( username );

            Text text = new Text();
            text.setY( 50 );
            text.setFont( Font.font( "Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 15 ) );
            text.setText( "You have logged in successfully" );
            width = text.getLayoutBounds().getWidth();
            text.setX( (root.getPrefWidth() - width) / 2 );

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
