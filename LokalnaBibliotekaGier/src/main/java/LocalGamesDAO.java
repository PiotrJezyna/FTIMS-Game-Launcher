import fgl.database.AbstractDao;
import fgl.product.Game;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LocalGamesDAO extends AbstractDao<Game> {

    @Override
    protected Game get(Long id) throws SQLException {
        return null;
    }

    @Override
    protected List<Game> getAll() throws SQLException {

        List<Game> localGames = new ArrayList<>();

        File f = new File("C:\\FtimsGameLauncher");
        String[] pathnames;
        pathnames = f.list();

        for (String pathname : pathnames) {

            File tmpDir = new File("C:\\FtimsGameLauncher\\" + pathname + "\\" + pathname + ".exe");
            if (tmpDir.exists())
            {
                connectSQL();

                String query = "SELECT ID, UserID, Tags, Path, UserCount, IsReported  FROM Games WHERE Title = '" + pathname + "'";

                stmt = conn.createStatement();
                rs = stmt.executeQuery( query );
                if (rs.next())
                {
                    System.out.println(pathname);

                    Long gameId = rs.getLong("ID");
                    Long userId = rs.getLong("UserID");
                    String tags = rs.getString("Tags");
                    String path = rs.getString("Path");
                    Integer userCount = rs.getInt("UserCount");
                    boolean isReported = rs.getBoolean("IsReported");

                    localGames.add(new Game(gameId, userId, pathname, tags, path, null, null, userCount, isReported));
                }

                disconnectSQL();
            }
        }

        return localGames;
    }

    @Override
    protected void insert(Game game) throws SQLException {

    }

    @Override
    protected void update(Game game) throws SQLException {

    }

    @Override
    protected void delete(Game game) throws SQLException {

    }
}
