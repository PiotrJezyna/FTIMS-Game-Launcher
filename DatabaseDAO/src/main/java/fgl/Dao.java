package fgl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Dao {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://remotemysql.com/5VexXpVWzU";

    private static final String USER = "5VexXpVWzU";
    private static final String PASS = "apQqybLdoW";

    private Connection conn = null;
    private Statement stmt = null;
    private ResultSet rs = null;

    public static void main(String[] args) {

    }
}
