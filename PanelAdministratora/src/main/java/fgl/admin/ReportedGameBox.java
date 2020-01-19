package fgl.admin;

import fgl.product.Game;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

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
        // TODO Show Product Card
        // ...
      }
    } );

    this.getChildren().addAll( label, discard, delete, show );
  }
}
