package fgl;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;

import java.util.List;

public abstract class Dao<T> {

     static final String DB_URL = "jdbc:mysql://remotemysql.com/5VexXpVWzU";

     static final String USER = "5VexXpVWzU";
     static final String PASS = "apQqybLdoW";

     Connection conn = null;
     Statement stmt = null;
     ResultSet rs = null;

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

    abstract T get(Long id);

    abstract List<T> getAll();

    abstract void insert(T t);

    abstract void update(T t);

    abstract void delete(T t);
}
