package fgl.product;

import java.sql.SQLException;
import java.util.List;

public class Application {

    public static void main( String[] args ) throws SQLException {

        System.out.println("Initializing some data..."); // <- It works.

        GameDAO dao = new GameDAO();

        /*Game game = new Game(2L, "Gra o żabie",
                "Platformer, Puzzle", "Horror",
                "Fajna giera polecam", "//GraoZabie");

        dao.insert(game);*/

        List<Game> games = dao.getAll();
        //game = games.get(games.size() - 1);

        //game.setReported(true);
        //dao.update(game);

        for(int i = 0; i < games.size(); i++)
            System.out.println(games.get(i).getUserCount());

        //System.out.println(game.getTitle()); // <- It works.

        //dao.delete(game);
    }
}
