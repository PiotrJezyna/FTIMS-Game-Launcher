package fgl.product;

import java.sql.SQLException;
import java.util.List;

public class Application {

    public static void main( String[] args ) throws SQLException {

        GameDAO dao = new GameDAO();

        Game game = new Game(2L, "Gra o żabie", "Platformer, Puzzle", "//GraoZabie");
        dao.insert(game);

        List<Game> games = dao.getAll();
        game = games.get(games.size() - 1);

        game.setReported(true);
        dao.update(game);

        dao.delete(game);
    }
}
