package fgl.userPanel;

import fgl.drive.DriveDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class EditUserProfileViewControl {

    private AnchorPane root;
    private AnchorPane avatarSpace;
    private File newAvatarPath;

    @FXML
    public TextField nameEditField;

    @FXML
    public TextField surnameEditField;

    @FXML
    public TextField usernameEditField;

    @FXML
    public TextField emailEditField;

    @FXML
    public Button buttonSaveChanges;

    @FXML
    private ImageView newAvatar;

    @FXML
    protected void initialize() {
        nameEditField.setText(getLoggedInUserName());
        surnameEditField.setText(getLoggedInUserSurname());
        usernameEditField.setText(getLoggedInUserUsername());
        emailEditField.setText(getLoggedInUserEmail());
    }

    public void init( AnchorPane root, AnchorPane avatarSpace ) {
        this.root = root;
        this.avatarSpace = avatarSpace;
    }

    private String getLoggedInUserName(){
        String name = UserSession.getUserSession().getCurrentUser().getName();
        return name;
    }
    private String getLoggedInUserSurname(){
        String surname = UserSession.getUserSession().getCurrentUser().getSurname();
        return surname;
    }
    private String getLoggedInUserUsername(){
        String username = UserSession.getUserSession().getCurrentUser().getUsername();
        return username;
    }
    private String getLoggedInUserEmail(){
        String email = UserSession.getUserSession().getCurrentUser().getEmail();
        return email;
    }

    public void updateUserData(javafx.event.ActionEvent actionEvent) throws IOException, SQLException {
        UserDAO dao = new UserDAO();
        DriveDao driveDao = new DriveDao();

        driveDao.uploadAvatar(UserSession.getUserSession().getCurrentUser().getUsername(), newAvatarPath.getAbsolutePath());
        File imagePath = driveDao.downloadAvatar(UserSession.getUserSession().getCurrentUser().getUsername());
        Image image = new Image( imagePath.toURI().toString(),
                120, 120, true, false );

        String name = nameEditField.getText();
        String surname = surnameEditField.getText();
        String username = usernameEditField.getText();
        String email = emailEditField.getText();

        Circle clip = new Circle(avatarSpace.getPrefWidth() / 2, 80, 52);
        ImageView avatar = new ImageView(image);
        avatar.setX(33);
        avatar.setY(20);
        avatar.setClip(clip);

        Text usernameText = new Text();
        usernameText.setFont( Font.font( "Verdana", FontWeight.NORMAL, FontPosture.REGULAR, 18 ) );
        usernameText.setText( username );
        usernameText.setY( 160 );
        double width = usernameText.getLayoutBounds().getWidth();
        usernameText.setX( (avatarSpace.getPrefWidth() - width) / 2 );

        avatarSpace.getChildren().clear();
        avatarSpace.getChildren().add( avatar );
        avatarSpace.getChildren().add( usernameText );


        User user = UserSession.getUserSession().getCurrentUser();
        //user = new User(name, surname, username, email);
        user.setName(name);
        user.setSurname(surname);
        user.setUsername(username);
        user.setEmail(email);
        if (checkForMailAndUsername(user.getId(), username, email)) {
            dao.update(user);

            backToUserProfile(null);
        } else {
            warningWindow("Warning", "User with this email/username already exists");
        }
    }

    public void backToUserProfile(javafx.event.ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource( "/UserProfileView.fxml" ));
        AnchorPane pane = loader.load();
        UserProfileViewControl ctrl = loader.getController();
        ctrl.init( root, avatarSpace );

        root.getChildren().clear();
        root.getChildren().add( pane );
    }

    private boolean checkForMailAndUsername(Long userID, String uUserName, String uEmail) throws SQLException {
        UserDAO dao = new UserDAO();
        List<User> allUsers = dao.getAll();
        boolean flag = true;

        for (int i = 0; i < allUsers.size(); i++) {
            if (!userID.equals(allUsers.get(i).getId()) && uUserName.equals(allUsers.get(i).getUsername())) {
                flag = false;
            } else if (!userID.equals(allUsers.get(i).getId()) && uEmail.equals(allUsers.get(i).getEmail())) {
                flag = false;
            }
        }
        return flag;
    }

    private void warningWindow(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void showFilePicker(ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);

        Stage window = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        newAvatarPath = fileChooser.showOpenDialog(window);
        newAvatar.setImage(new Image(newAvatarPath.toURI().toString(), newAvatar.getFitWidth(), newAvatar.getFitHeight(), true, false));
    }
}
