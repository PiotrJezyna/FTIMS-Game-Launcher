package fgl.product;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ProductCard.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Katalog produktu");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        GameManager gm = loader.getController();

        gm.ShowProductCard("Not Tetris");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
