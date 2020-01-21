package fgl.catalog;

import fgl.userPanel.UserSession;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import fgl.userPanel.Login;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/CatalogCard.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/UserCard.fxml"));
        primaryStage.setTitle("Karta ocen gier");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        Login login = new Login();
        try {
            login.validate("js", "js");
        }
        catch (Exception e) {}

        System.out.println(UserSession.getUserSession().getCurrentUser().getName());
        launch(args);
    }
}