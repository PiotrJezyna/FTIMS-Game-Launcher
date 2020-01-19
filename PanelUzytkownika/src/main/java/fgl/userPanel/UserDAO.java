package fgl.userPanel;

import fgl.database.AbstractDao;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends AbstractDao<User> {

    @Override
    public User get( Long id ) throws SQLException {

        connectSQL();

        try {
            String query = "SELECT Name, Surname, Username, Email, Type, IsActivated, IsBlocked FROM Users WHERE id = %s";
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
            boolean isActivated = rs.getBoolean("IsActivated");

            return new User(id, first, last, username, email, userType, isActivated, isBlocked);

        } catch ( SQLException e ) {

            throw new SQLException(e.getMessage());

        } finally {

            rs.close();
            stmt.close();
            disconnectSQL();
        }
    }

    @Override
    public List<User> getAll() throws SQLException {

        connectSQL();

        try {

            String query = "SELECT id, Name, Surname, Username, Email, Type, IsActivated, IsBlocked, Password FROM Users";
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
                boolean isActivated = rs.getBoolean("IsActivated");
                boolean isBlocked = rs.getBoolean("IsBlocked");
                String password = rs.getString("Password");

                User userSQL = new User(id, first, last, username, email, userType, isActivated, isBlocked, password);
                users.add( userSQL );
            }

            return users;

        } catch (SQLException e ) {

            throw new SQLException(e.getMessage());

        } finally {

            rs.close();
            stmt.close();
            disconnectSQL();
        }
    }

    @Override
    public void insert( User user ) throws SQLException {

        connectSQL();

        String query = "INSERT INTO Users(Username, Email, Name, Surname, Password) VALUES ('%s', '%s', '%s','%s','%s')";
        query = String.format(query, user.getUsername(), user.getEmail(), user.getName(), user.getSurname(), user.getPassword());

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
    public void update( User user ) throws SQLException {

        connectSQL();

        String query =
                "UPDATE Users " +
                        "SET " +
                        "Name = '%s', " +
                        "Surname = '%s', " +
                        "Username = '%s', " +
                        "Email = '%s', " +
                        "Type = '%s', " +
                        "IsActivated = '%s' " +
                        "IsBlocked = '%s' " +
                        "WHERE ID = " + user.getId();

        query = String.format(query, user.getName(), user.getSurname(), user.getUsername(), user.getEmail(), user.getType(), user.isActivated(), user.isBlocked());
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
    public void delete( User user ) throws SQLException {

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