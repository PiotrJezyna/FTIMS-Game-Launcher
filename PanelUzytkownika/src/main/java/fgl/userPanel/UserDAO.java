package fgl.userPanel;

import fgl.database.AbstractDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends AbstractDao<User> {

    @Override
    protected User get( Long id ) throws SQLException {

        connectSQL();

        try {

            String query = "SELECT Name, Surname, Username, Email, IsBlocked FROM Users WHERE id = %s";
            query = String.format(query, id);
            System.out.println(query);
            stmt = conn.createStatement();
            rs = stmt.executeQuery( query );

            rs.next();

            String first = rs.getString("Name");
            String last = rs.getString("Surname");
            String username = rs.getString("Username");
            String email = rs.getString("Email");
            String type = rs.getString( "Type" );
            UserType userType;
            if ( type.contentEquals(UserType.ADMINISTRATOR.name) ) {
                userType = UserType.ADMINISTRATOR;
            } else if ( type.contentEquals(UserType.MODERATOR.name) ) {
                userType = UserType.MODERATOR;
            } else {
                userType = UserType.USER;
            }
            boolean isBlocked = rs.getBoolean("IsBlocked");

            return new User(id, first, last, username, email, userType, isBlocked);

        } catch ( SQLException e ) {

            throw new SQLException(e.getMessage());

        } finally {

            rs.close();
            stmt.close();
            disconnectSQL();
        }
    }

    @Override
    protected List<User> getAll() throws SQLException {

        connectSQL();

        try {

            String query = "SELECT id, Name, Surname, Username, Email, IsBlocked FROM Users";
            stmt = conn.createStatement();
            rs = stmt.executeQuery( query );

            List<User> users = new ArrayList<User>();

            while ( rs.next() ) {
                Long id  = rs.getLong( "id" );
                String first = rs.getString( "Name" );
                String last = rs.getString( "Surname" );
                String username = rs.getString( "Username" );
                String email = rs.getString( "Email" );
                String type = rs.getString( "Type" );
                UserType userType;
                if ( type.contentEquals(UserType.ADMINISTRATOR.name) ) {
                    userType = UserType.ADMINISTRATOR;
                } else if ( type.contentEquals(UserType.MODERATOR.name) ) {
                    userType = UserType.MODERATOR;
                } else {
                    userType = UserType.USER;
                }
                boolean isBlocked = rs.getBoolean("IsBlocked");

                User userSQL = new User(id, first, last, username, email, userType, isBlocked);
                users.add( userSQL );
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
    protected void insert( User user ) throws SQLException {

        connectSQL();

        String query = "INSERT INTO Users(Username, Email) VALUES ('%s', '%s')";
        query = String.format(query, user.getUsername(), user.getEmail());

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
    protected void update( User user ) throws SQLException {

        connectSQL();

        String query =
                "UPDATE Users " +
                "SET " +
                "Name = '%s', " +
                "Surname = '%s', " +
                "Username = '%s', " +
                "Email = '%s', " +
                "Type = '%s', " +
                "IsBlocked = '%s' " +
                "WHERE ID = " + user.getId();

        query = String.format(query, user.getName(), user.getSurname(), user.getUsername(), user.getEmail(), user.getType(), user.isBlocked());
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
    protected void delete( User user ) throws SQLException {

        connectSQL();

        String query = "DELETE FROM Users WHERE ID = %s";
        query = String.format(query, user.getId());

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
