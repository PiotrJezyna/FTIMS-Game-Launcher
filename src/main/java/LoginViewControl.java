import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
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

    public CheckBox rememberMeCheckbox;
    @FXML
    private TextField username, password;
    private static UserSession userSession;

    String uUsername, uPassword;

    @FXML
    protected void initialize() throws IOException {
        if (username.getText().equals("") && password.getText().equals("") && !readCredentialsFromFile("C:\\Users\\admin\\IdeaProjects\\UserProfile\\login.txt").equals("null") && !readCredentialsFromFile("C:\\Users\\admin\\IdeaProjects\\UserProfile\\password.txt").equals("null") ) {
            username.setText(readCredentialsFromFile("C:\\Users\\admin\\IdeaProjects\\UserProfile\\login.txt"));
            password.setText(readCredentialsFromFile("C:\\Users\\admin\\IdeaProjects\\UserProfile\\password.txt"));
            rememberMeCheckbox.setSelected(true);
        }
    }

    public void openRegisterScene(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("RegisterView.fxml"));

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);

        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();

        window.setScene(scene);
        window.initStyle(StageStyle.TRANSPARENT);
        window.show();
    }

    public void loginButtonClicked(ActionEvent actionEvent) throws SQLException, IOException, NoSuchAlgorithmException {
        uUsername = username.getText();
        uPassword =password.getText();
        String notEncryptedPassword = uPassword;
        uPassword = encryptPassword(password.getText());
        Login login = new Login();
        if(login.validate(uUsername, uPassword)){

            if (rememberMeCheckbox.isSelected()) {
                writeCredentialsToFile("C:\\Users\\admin\\IdeaProjects\\UserProfile\\login.txt", uUsername);
                writeCredentialsToFile("C:\\Users\\admin\\IdeaProjects\\UserProfile\\password.txt", notEncryptedPassword);
            } else {
                writeCredentialsToFile("C:\\Users\\admin\\IdeaProjects\\UserProfile\\login.txt", "null");
                writeCredentialsToFile("C:\\Users\\admin\\IdeaProjects\\UserProfile\\password.txt", "null");
            }


            Parent root = FXMLLoader.load(getClass().getResource("UserProfileView.fxml"));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.initStyle(StageStyle.TRANSPARENT);
            window.show();
        } else {
            warningWindow("Warning", "Wrong credentials. Please try again");
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


    ///////////////////////////         IMPORTANT       ////////////////////////////////////
    ///////////////////////////randomString musi zostac wyslany emailem!!!!!
    public void goToConfirmCodeFromEmail(ActionEvent actionEvent) throws IOException, SQLException {
        UserDAO dao = new UserDAO();
        List<User> allUsers = dao.getAll();
        String randomString = generateRandomString();

        uUsername = username.getText();
        if (!uUsername.equals("")) {
            for (int i = 0; i < allUsers.size(); i++) {
                if (uUsername.equals(allUsers.get(i).getUsername())) {
                    User user = allUsers.get(i);
                    user.setConfirmationString(randomString);
                    userSession = new UserSession(user);
                }
            }
            Parent root = FXMLLoader.load(getClass().getResource("ConfirmChangingPasswordView.fxml"));
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.initStyle(StageStyle.TRANSPARENT);
            window.show();
        } else {
            warningWindow("Warning", "If you want to change your password please put your username");
        }

    }

    public static UserSession getUserSession() {
        return userSession;
    }

    public static void setUserSession(UserSession userSession) {
        LoginViewControl.userSession = userSession;
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
        BufferedReader reader = new BufferedReader(new FileReader(fileDirectory));
        String currentLine = reader.readLine();
        reader.close();
        return currentLine;

    }
}
