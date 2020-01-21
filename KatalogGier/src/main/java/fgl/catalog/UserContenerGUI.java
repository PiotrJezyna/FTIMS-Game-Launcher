package fgl.catalog;

import fgl.userPanel.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class UserContenerGUI {

    private UserContener userContener = new UserContener();

    private int recordPerPage = 5;
    private int pageNumber = 0;
    private int pageCount = 0;

    public Button prevPage;
    public Button nextPage;

    @FXML
    private AnchorPane root;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox usersBox;
    @FXML
    private TextField phraseField;

    @FXML
    public void setSearchPhrase(String searchPhrase) {
        userContener.setSearchPhrase(searchPhrase);
    }

    public void initialize() throws Exception {
        scrollPane.setContent(usersBox);
        main(null);
    }

    public void main(String[] args) throws Exception {
        displayUsers();
    }

    public void displayUsers() throws Exception {
        usersBox.getChildren().clear();

        List<User> displayedUsers = userContener.getDisplayedUsers();

        pageCount = (int)Math.ceil((double) userContener.getRecordCount() / recordPerPage);

        int start = pageNumber * recordPerPage;
        int length = ((displayedUsers.size() - start) < recordPerPage) ? (displayedUsers.size() - start) : recordPerPage;

        for (int i = start; i < start + length; ++i) {
            User user = displayedUsers.get(i);

            HBox userBox = new HBox();

            Label label_userName = new Label(user.getUsername());
            Label label_name = new Label(user.getName());
            Label label_surname = new Label(user.getSurname());
            Label label_email = new Label(user.getEmail());

            userBox.getChildren().add(label_userName);
            userBox.getChildren().add(label_name);
            userBox.getChildren().add(label_surname);
            userBox.getChildren().add(label_email);

            HBox.setMargin(label_userName, new Insets(0, 10, 0, 10));
            HBox.setMargin(label_name, new Insets(0, 10, 0, 10));
            HBox.setMargin(label_surname, new Insets(0, 10, 0, 10));
            HBox.setMargin(label_email, new Insets(0, 10, 0, 10));

            VBox.setMargin(userBox, new Insets(10, 0, 5, 0));

            usersBox.getChildren().add(userBox);
        }
    }


    @FXML
    void buttonOnAction(ActionEvent event) throws Exception {

        setSearchPhrase(phraseField.getText());
        pageNumber = 0;
        displayUsers();
    }

    @FXML
    void handlePageButton(ActionEvent event) throws Exception {

        boolean dirtyFlag = false;

        if (prevPage.isHover()) {
            if (pageNumber > 0) {
                pageNumber--;
                dirtyFlag = true;
            }
        }
        if (nextPage.isHover()) {
            if (pageNumber < pageCount - 1) {
                pageNumber++;
                dirtyFlag = true;
            }
        }

        if (!dirtyFlag)
            return;

        displayUsers();
    }


}
