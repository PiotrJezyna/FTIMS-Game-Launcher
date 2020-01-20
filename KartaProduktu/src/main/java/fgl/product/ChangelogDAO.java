package fgl.product;

import fgl.database.AbstractDao;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import java.sql.Date;

public class ChangelogDAO extends AbstractDao<Changelog> {

    @Override
    protected Changelog get(Long id) throws SQLException {

        String query = "SELECT ID, GameID, Version, Description, Date " +
                "FROM Changelog WHERE ID = %s";
        query = String.format(query, id);

        System.out.println(query);

        stmt = conn.createStatement();
        rs = stmt.executeQuery( query );
        rs.next();

        Long ID = rs.getLong("ID");
        Long gameID = rs.getLong("GameID");
        Long version = rs.getLong("Version");
        String description = rs.getString("Description");
        Date date = rs.getDate("Date");

        return new Changelog(ID, gameID, version, description, date, null);
    }

    public Changelog get(Game game) throws SQLException {

        connectSQL();

        try {

            String query = "SELECT ID, GameID, Version, Description, Date " +
                "FROM Changelog WHERE GameID = %s";
            query = String.format(query, game.getId());

            System.out.println(query);

            stmt = conn.createStatement();
            rs = stmt.executeQuery( query );

            Long ID;
            Long gameID;
            Long version;
            String description;
            java.sql.Date date;

            if(rs.next())
            {
                ID = rs.getLong("ID");
                gameID = rs.getLong("GameID");
                version = rs.getLong("Version");
                description = rs.getString("Description");
                date = rs.getDate("Date");
            }
            else
            {
                ID = 0L;
                gameID = game.getId();
                version = 0L;
                description = "";
                date = new java.sql.Date(new Date().getTime());
            }

            return new Changelog(ID, gameID, version, description, date, game.getTitle());

        } catch ( SQLException e ) {

            throw new SQLException(e.getMessage());

        } finally {

            rs.close();
            stmt.close();
            disconnectSQL();
        }
    }

    @Override
    protected List<Changelog> getAll() throws SQLException {

        connectSQL();

        try {

            String query = "SELECT ID, GameID, Version, Description, Date FROM Changelog ORDER BY ID DESC";
            stmt = conn.createStatement();
            rs = stmt.executeQuery( query );

            List<Changelog> changelogs = new ArrayList<Changelog>();

            while ( rs.next() ) {

                Long ID = rs.getLong("ID");
                Long gameID = rs.getLong("GameID");
                Long version = rs.getLong("Version");
                String description = rs.getString("Description");
                Date date = rs.getDate("Date");

                Changelog changelog = new Changelog(ID, gameID, version, description, date, null);
                changelogs.add(changelog);
            }

            return changelogs;

        } catch ( SQLException e ) {

            throw new SQLException(e.getMessage());

        } finally {

            rs.close();
            stmt.close();
            disconnectSQL();
        }
    }

    @Override
    protected void insert(Changelog changelog) throws SQLException {

        connectSQL();

        String query = "INSERT INTO Changelog(GameID, Version, Description, Date) VALUES (%s, '%s', '%s', '%s')";
        query = String.format(query, changelog.getGameId(), changelog.getVersion(), changelog.getDescription(), changelog.getDate());

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
    protected void update(Changelog changelog) throws SQLException {

        connectSQL();

        String query =
                "UPDATE Changelog " +
                        "SET " +
                        "GameID = %s, " +
                        "Version = '%s', " +
                        "Description = '%s', " +
                        "Date = '%s' " +
                        "WHERE ID = " + changelog.getID();

        query = String.format(query, changelog.getGameId(), changelog.getVersion(), changelog.getDescription(), changelog.getDate());

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
    protected void delete(Changelog changelog) throws SQLException {


        connectSQL();

        String query = "DELETE FROM Changelog WHERE ID = %s";
        query = String.format(query, changelog.getID());

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
