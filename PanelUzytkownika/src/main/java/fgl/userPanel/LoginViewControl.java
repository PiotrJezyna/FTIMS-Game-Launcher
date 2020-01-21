package fgl.userPanel;

import fgl.communication.MailHandler;
import fgl.drive.DriveDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class LoginViewControl {

    private AnchorPane root;
    private AnchorPane avatarSpace;

    @FXML
    private TextField username, password;

    @FXML
    private CheckBox rememberMeCheckbox;

    private String uUsername, uPassword;

    private final String CREDENTIALS_PATH = System.getProperty( "user.dir" ) + "\\credentials";

    public boolean init( AnchorPane root, AnchorPane avatarSpace ) {
        this.root = root;
        this.avatarSpace = avatarSpace;

        try {
            if (username.getText().equals("") && password.getText().equals("") &&
                    !readCredentialsFromFile(CREDENTIALS_PATH + "\\login.txt").isEmpty() &&
                    !readCredentialsFromFile(CREDENTIALS_PATH + "\\password.txt").isEmpty() ) {
                username.setText(readCredentialsFromFile(CREDENTIALS_PATH + "\\login.txt"));
                password.setText(readCredentialsFromFile(CREDENTIALS_PATH + "\\password.txt"));
                rememberMeCheckbox.setSelected(true);
                loginButtonClicked( null );
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void openRegisterScene(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource( "/RegisterView.fxml" ));
        AnchorPane pane = loader.load();
        RegisterViewControl ctrl = loader.getController();
        ctrl.init( root, avatarSpace );

        double width = pane.getPrefWidth();
        pane.setLayoutX( (root.getPrefWidth() - width) / 2 );

        root.getChildren().clear();
        root.getChildren().add( pane );
    }

    public void loginButtonClicked(ActionEvent actionEvent) throws SQLException, IOException, NoSuchAlgorithmException {

        uUsername = username.getText();
        uPassword = encryptPassword( password.getText() );
        if (actionEvent == null) {
            uPassword = password.getText();
        }

        Login login = new Login();
        if( login.validate( uUsername, uPassword ) ) {
            if (rememberMeCheckbox.isSelected()) {
                writeCredentialsToFile(CREDENTIALS_PATH + "\\login.txt", uUsername);
                writeCredentialsToFile(CREDENTIALS_PATH + "\\password.txt", uPassword);
            }

            DriveDao driveDao = new DriveDao();
            User user = UserSession.getUserSession().getCurrentUser();
            Image image = new Image( driveDao.downloadAvatar( user.getUsername() )
                    .toURI().toString(), 120, 120, true, false );
            Circle clip = new Circle(avatarSpace.getPrefWidth() / 2, 80, 55);
            ImageView avatar = new ImageView(image);
            avatar.setX(33);
            avatar.setY(20);
            avatar.setClip(clip);

            Text username = new Text();
            username.setFont( Font.font( "Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 18 ) );
            username.setText( uUsername );
            username.setY( 160 );
            double width = username.getLayoutBounds().getWidth();
            username.setX( (avatarSpace.getPrefWidth() - width) / 2 );

            avatarSpace.getChildren().add( avatar );
            avatarSpace.getChildren().add( username );

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

    public void goToConfirmCodeFromEmail(ActionEvent actionEvent) throws IOException, SQLException {
        UserDAO dao = new UserDAO();
        List<User> allUsers = dao.getAll();
        String randomString = generateRandomString();
        System.out.println(randomString);

        uUsername = username.getText();
        User user = null;
        if (!uUsername.equals("")) {
            for (int i = 0; i < allUsers.size(); i++) {
                if (uUsername.equals(allUsers.get(i).getUsername())) {
                    user = allUsers.get(i);
                    user.setConfirmationString(randomString);
                    UserSession.getUserSession().setConfirmationCode(randomString);

                    MailHandler.sendMailWithCode( user.getUsername(), user.getEmail(), "reminder", randomString );
                }
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource( "/ConfirmChangingPasswordView.fxml" ));
            AnchorPane pane = loader.load();
            ConfirmChangingPasswordViewControl ctrl = loader.getController();
            ctrl.init( root, avatarSpace, user );

            double width = pane.getPrefWidth();
            pane.setLayoutX( (root.getPrefWidth() - width) / 2 );

            root.getChildren().clear();
            root.getChildren().add( pane );
        } else {
            warningWindow("Warning", "If you want to change your password please put your username");
        }

    }

    public String generateRandomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }

    public void writeCredentialsToFile(String fileName, String credential)
            throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
        writer.write(credential);

        writer.close();
    }

    public String readCredentialsFromFile(String fileDirectory)
            throws IOException {
        File file = new File(fileDirectory);
        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(fileDirectory));
            String currentLine = reader.readLine();
            reader.close();

            return currentLine;
        }

        return "";
    }
}
