package fgl.admin;

import fgl.product.Game;
import fgl.product.GameManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ReportedGameBox extends HBox {
  ReportedGameBox( Game game ) {
    super();

    Label label = new Label();
    label.setText( game.getTitle() );
    label.setMaxWidth( Double.MAX_VALUE );
    HBox.setHgrow( label, Priority.ALWAYS );

    Button discard = new Button( "Discard" );
    discard.setOnAction( new EventHandler<ActionEvent>() {
      @Override
      public void handle( ActionEvent event ) {
        ModerationPanel.discardReport( game );
      }
    } );

    Button delete = new Button( "Delete" );
    delete.setOnAction( new EventHandler<ActionEvent>() {
      @Override
      public void handle( ActionEvent event ) {
        ModerationPanel.deleteGame( game );
      }
    } );

    Button show = new Button( "Show game card" );
    show.setOnAction( new EventHandler<ActionEvent>() {
      @Override
      public void handle( ActionEvent event ) {
        try {
          FXMLLoader fxmlLoader = new FXMLLoader(
                  getClass().getResource( "/ProductCard.fxml" )
          );
          Parent root = fxmlLoader.load();
          Stage stage = new Stage();
          stage.setTitle( "Reported Game Product Card" );
          stage.setScene( new Scene( root ) );
          stage.show();
          GameManager gm = fxmlLoader.getController();
          gm.ShowProductCard( game );
        } catch ( IOException e ) {
          //TODO hande io exception
        } catch ( SQLException e ) {
          //TODO hande sql exception
        }
      }
    } );

    this.getChildren().addAll( label, discard, delete, show );
  }
}
