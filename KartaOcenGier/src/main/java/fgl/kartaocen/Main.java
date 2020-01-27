package fgl.kartaocen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/RatingsCard.fxml"));

        //todo:only test
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/RatingsCard.fxml"));
//        Parent root = loader.load();

        primaryStage.setTitle("Karta ocen gier");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();


        //todo:only test
//        ReviewCard r = loader.getController();
//        r.setGame(1L); //nottetris
//        r.setGame(2L); //witcher
//        r.setLoggedUser(1L);//author
//        r.setLoggedUser(2L);
//        r.setLoggedUser(91L);
//        r.setLoggedUser(92L);
//        r.setLoggedUser(93L);
//        r.init();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
