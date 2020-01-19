package fgl.kartaocen;

import fgl.database.AbstractDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReviewDao extends AbstractDao<Review> {

    @Override
    public Review get(Long id) throws SQLException {

        connectSQL();
        try {
            String query = "SELECT * from Opinions";

            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            rs.next();
                Long ID = rs.getLong("ID");
                Long user = rs.getLong("UserID");
                Long game = rs.getLong("GameID");
                int rate = rs.getInt("Rate");
                java.util.Date date = rs.getDate("date");
                String comment = rs.getString("Comment");

            return new Review(ID,comment, rate,comment,date,game,user );
        }
         catch ( SQLException e ) {

            throw new SQLException( e.getMessage() );

        } finally {

            rs.close();
            stmt.close();
            disconnectSQL();
        }

    }

    @Override
    public List<Review> getAll() throws SQLException{
        connectSQL();
        try {
            List<Review> reviews = new ArrayList<>();

            String query = "SELECT * from Opinions";

            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            while (rs.next()) {
                Long ID = rs.getLong("ID");
                Long user = rs.getLong("UserID");
                Long game = rs.getLong("GameID");
                int rate = rs.getInt("Rate");
                java.util.Date date = rs.getDate("date");
                int opinion = rs.getInt("opinion");
                String comment = rs.getString("Comment");
                String authorsReply = rs.getString("Reply");

                reviews.add(new Review(ID,comment,rate,authorsReply,date,game,user));

            }
            return reviews;
        }
        catch ( SQLException e ) {

            throw new SQLException( e.getMessage() );

        } finally {

            rs.close();
            stmt.close();
            disconnectSQL();
        }

    }
    @Override
    protected void insert(Review review) throws SQLException {
        connectSQL();

        String query = "INSERT INTO Opinions(UserID, GameID, Date, Comment, Rate ) VALUES ( '%d', '%d', '%s', '%s','%d')";
        query = String.format( query, review.getUser(), review.getGame(),new java.sql.Date(Calendar.getInstance().getTime().getTime()),review.getComment(),review.getRating() );

        System.out.println( query );

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate( query );
        } catch ( SQLException e ) {
            throw new SQLException( e.getMessage() );
        } finally {
            disconnectSQL();
        }
    }

    @Override
    protected void update(Review review) throws SQLException {
        connectSQL();

        String query ="UPDATE Opinions SET Reply = %s";

        query = String.format(query, review.getAuthorsReply());
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
    protected void delete(Review review) throws SQLException {

    }
}
