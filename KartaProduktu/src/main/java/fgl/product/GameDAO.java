package fgl.product;

import fgl.database.AbstractDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameDAO extends AbstractDao<Game> {

    @Override
    protected Game get( Long id ) throws SQLException {

        connectSQL();

        try {

            String query = "SELECT UserID, Title, Tags, Path, UserCount, IsReported " +
                           "FROM Games WHERE ID = %s";
            query = String.format(query, id);

            System.out.println(query);

            stmt = conn.createStatement();
            rs = stmt.executeQuery( query );
            rs.next();

            Long userId = rs.getLong("UserID");
            String title = rs.getString("Title");
            String tags = rs.getString("Tags");
            String path = rs.getString("Path");
            Integer userCount = rs.getInt("UserCount");
            boolean isReported = rs.getBoolean("IsReported");

            return new Game(id, userId, title, tags, path, userCount, isReported);

        } catch ( SQLException e ) {

            throw new SQLException(e.getMessage());

        } finally {

            rs.close();
            stmt.close();
            disconnectSQL();
        }
    }

    @Override
    protected List<Game> getAll() throws SQLException {

        connectSQL();

        try {

            String query = "SELECT ID, UserID, Title, Tags, Path, UserCount, IsReported FROM Games";
            stmt = conn.createStatement();
            rs = stmt.executeQuery( query );

            List<Game> users = new ArrayList<Game>();

            while ( rs.next() ) {

                Long gameId = rs.getLong("ID");
                Long userId = rs.getLong("UserID");
                String title = rs.getString("Title");
                String tags = rs.getString("Tags");
                String path = rs.getString("Path");
                Integer userCount = rs.getInt("UserCount");
                boolean isReported = rs.getBoolean("IsReported");

                Game game = new Game(gameId, userId, title, tags, path, userCount, isReported);
                users.add( game );
            }

            return users;

        } catch ( SQLException e ) {

            throw new SQLException(e.getMessage());

        } finally {

            rs.close();
            stmt.close();
            disconnectSQL();
        }
    }

    @Override
    protected void insert( Game game ) throws SQLException {

        connectSQL();

        String query = "INSERT INTO Games(UserID, Title, Tags, Path) VALUES (%s, '%s', '%s', '%s')";
        query = String.format(query, game.getUserId(), game.getTitle(), game.getTags(), game.getPath());

        System.out.println( query );

        try {

            stmt = conn.createStatement();
            stmt.executeUpdate( query );

        } catch ( SQLException e ) {

            throw new SQLException(e.getMessage());

        } finally {

            disconnectSQL();
        }
    }

    @Override
    protected void update( Game game ) throws SQLException {

        connectSQL();

        String query =
                "UPDATE Games " +
                        "SET " +
                        "UserID = %s, " +
                        "Title = '%s', " +
                        "Tags = '%s', " +
                        "Path = '%s', " +
                        "UserCount = %s, " +
                        "IsReported = %s " +
                        "WHERE ID = " + game.getId();

        query = String.format(query, game.getUserId(), game.getTitle(), game.getTags(), game.getPath(), game.getUserCount(), game.isReported());
        query = query.replace("false", "0");
        query = query.replace("true", "1");

        System.out.println( query );

        try {

            stmt = conn.createStatement();
            stmt.executeUpdate( query );

        } catch ( SQLException e ) {

            throw new SQLException(e.getMessage());

        } finally {

            disconnectSQL();
        }
    }

    @Override
    protected void delete( Game game ) throws SQLException {

        connectSQL();

        String query = "DELETE FROM Games WHERE ID = %s";
        query = String.format(query, game.getId());

        System.out.println( query );

        try {

            stmt = conn.createStatement();
            stmt.executeUpdate( query );

        } catch ( SQLException e ) {

            throw new SQLException(e.getMessage());

        } finally {

            disconnectSQL();
        }
    }
}
