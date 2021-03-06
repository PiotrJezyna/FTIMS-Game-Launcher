package fgl.product;

import fgl.database.AbstractDao;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameDAO extends AbstractDao<Game> {

    @Override
    public Game get( Long id ) throws SQLException {

        connectSQL();

        try {

            String query = "SELECT UserID, Title, Version, Description, Tags, UserCount, IsReported " +
                           "FROM Games WHERE ID = %s AND IsDeleted = 0";
            query = String.format(query, id);

            System.out.println(query);

            stmt = conn.createStatement();
            rs = stmt.executeQuery( query );
            rs.next();

            Long userId = rs.getLong("UserID");
            String title = rs.getString("Title");
            Integer version = rs.getInt("Version");
            String description = rs.getString("Description");
            String tags = rs.getString("Tags");
            Integer userCount = rs.getInt("UserCount");
            boolean isReported = rs.getBoolean("IsReported");

            return new Game( id, userId, title, version, tags, null, description, userCount, isReported );

        } catch ( SQLException e ) {

            throw new SQLException(e.getMessage());

        } finally {

            rs.close();
            stmt.close();
            disconnectSQL();
        }
    }

    public Game get( String title ) throws SQLException {

        connectSQL();

        try {

            String query = "SELECT UserID, Title, Version, Description, Tags, UserCount, IsReported " +
                    "FROM Games WHERE Title = '%s'";
            query = String.format(query, title);

            System.out.println(query);

            stmt = conn.createStatement();
            rs = stmt.executeQuery( query );
            rs.next();

            Long userId = rs.getLong("UserID");
            Long id = rs.getLong("ID");
            String description = rs.getString("Description");
            Integer version = rs.getInt("Version");
            String tags = rs.getString("Tags");
            Integer userCount = rs.getInt("UserCount");
            boolean isReported = rs.getBoolean("IsReported");

            return new Game(id, userId, title, version, tags, null, description, userCount, isReported);

        } catch ( SQLException e ) {

            throw new SQLException(e.getMessage());

        } finally {

            rs.close();
            stmt.close();
            disconnectSQL();
        }
    }


    @Override
    public List<Game> getAll() throws SQLException {

        connectSQL();

        try {

            String query = "SELECT ID, UserID, Title, Description, Version, Tags, UserCount, IsReported FROM Games " +
                    "WHERE IsDeleted = 0";
            stmt = conn.createStatement();
            rs = stmt.executeQuery( query );

            List<Game> games = new ArrayList<Game>();

            while ( rs.next() ) {

                Long gameId = rs.getLong("ID");
                Long userId = rs.getLong("UserID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                Integer version = rs.getInt("Version");
                String tags = rs.getString("Tags");
                Integer userCount = rs.getInt("UserCount");
                boolean isReported = rs.getBoolean("IsReported");

                Game game = new Game( gameId, userId, title, version, tags, null, description, userCount, isReported );
                games.add( game );
            }

            return games;

        } catch ( SQLException e ) {

            throw new SQLException(e.getMessage());

        } finally {

            rs.close();
            stmt.close();
            disconnectSQL();
        }
    }

    public List<Game> getAllWithQuery(String query) throws SQLException {

        connectSQL();

        try {

            //String query = new String(query_);
            stmt = conn.createStatement();
            rs = stmt.executeQuery( query );

            List<Game> games = new ArrayList<Game>();

            while ( rs.next() ) {

                Long gameId = rs.getLong("ID");
                Long userId = rs.getLong("UserID");
                String title = rs.getString("Title");
                Integer version = rs.getInt("Version");
                String tags = rs.getString("Tags");
                Integer userCount = rs.getInt("UserCount");
                boolean isReported = rs.getBoolean("IsReported");

                Game game = new Game( gameId, userId, title, version, tags, null, null, userCount, isReported );
                games.add( game );
            }

            return games;

        } catch ( SQLException e ) {

            throw new SQLException(e.getMessage());

        } finally {

            rs.close();
            stmt.close();
            disconnectSQL();
        }
    }

    @Override
    public void insert( Game game ) throws SQLException {

        connectSQL();

        String query = "INSERT INTO Games(UserID, Title, Version, Description, Tags) VALUES (%s, '%s', '%s', '%s', '%s')";
        query = String.format( query, game.getUserId(), game.getTitle(), game.getVersion(), game.getDescription(), game.getTags() );

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
    public void update( Game game ) throws SQLException {

        connectSQL();

        String query =
                "UPDATE Games " +
                        "SET " +
                        "UserID = %s, " +
                        "Title = '%s', " +
                        "Description = '%s', " +
                        "Version = %s, " +
                        "Tags = '%s', " +
                        "UserCount = %s, " +
                        "IsReported = %s, " +
                        "IsDeleted = %s " +
                        "WHERE ID = " + game.getId();

        query = String.format(query, game.getUserId(), game.getTitle(), game.getDescription(), game.getVersion(), game.getTags(), game.getUserCount(), game.isReported(), game.isDeleted());
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

    /**
     * DEPRACATED, DO NOT USE
     * @param game
     * @throws SQLException
     */
    @Deprecated
    @Override
    public void delete( Game game ) throws SQLException {

        connectSQL();

        String query = "DELETE FROM Games WHERE ID = %s";
        query = String.format( query, game.getId() );

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

    public float GetTimeInGame(Game game) throws SQLException {

        connectSQL();

        float timeInGame = 0;

        try {

            String query = "SELECT PlayTime FROM Users_Games WHERE GameID = %s";
            query = String.format(query, game.getId());

            System.out.println(query);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while(rs.next())
            {
                timeInGame += rs.getFloat("PlayTime");
            }

        } catch (SQLException e) {

            throw new SQLException(e.getMessage());

        } finally {

            rs.close();
            stmt.close();
            disconnectSQL();
        }

        return timeInGame;
    }

    public float GetTimeInGamePerUser(Game game) throws SQLException {

        float timeInGame = GetTimeInGame(game);
        ArrayList<Long> users = new ArrayList<Long>();

        connectSQL();

        try {

            String query = "SELECT UserID FROM Users_Games WHERE GameID = %s";
            query = String.format(query, game.getId());

            System.out.println(query);

            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while(rs.next())
            {
                Long currentUser = rs.getLong("UserID");

                if(!users.contains(currentUser))
                    users.add(currentUser);
            }

        } catch (SQLException e) {

            throw new SQLException(e.getMessage());

        } finally {

            rs.close();
            stmt.close();
            disconnectSQL();
        }

        if(users.size() == 0)
            return 0;
        else
            return timeInGame / (float)users.size();
    }
}
