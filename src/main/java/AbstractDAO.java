import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;

import java.util.List;

abstract class AbstractDao<T> {

    static final String DB_URL = "jdbc:mysql://remotemysql.com/5VexXpVWzU";

    static final String USER = "5VexXpVWzU";
    static final String PASS = "apQqybLdoW";

    protected Connection conn = null;
    protected Statement stmt = null;
    protected ResultSet rs = null;

    /**
     * Open connection with MySQL DB.
     *
     * @throws SQLException
     */
    public void connectSQL() throws SQLException {
        conn = DriverManager.getConnection( DB_URL, USER, PASS );
    }

    /**
     * Close connection with MySQL DB.
     *
     * @throws SQLException
     */
    public void disconnectSQL() throws SQLException {
        conn.close();
    }

    protected abstract T get( Long id ) throws SQLException;

    protected abstract List<T> getAll() throws SQLException;

    protected abstract void insert( T t ) throws SQLException;

    protected abstract void update( T t ) throws SQLException;

    protected abstract void delete( T t ) throws SQLException;
}