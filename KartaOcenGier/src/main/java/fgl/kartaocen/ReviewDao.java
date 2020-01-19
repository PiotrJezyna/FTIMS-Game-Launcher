// ////////////////////////////////////////////////////////////////// Package //
package fgl.kartaocen;

// ////////////////////////////////////////////////////////////////// Imports //
// =================================================================== FGL == //
import fgl.database.AbstractDao;
import fgl.product.GameDAO;
import fgl.userPanel.UserDAO;

// ================================================================= Other == //
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// ///////////////////////////////////////////////////////// Class: ReviewDao //
public class ReviewDao
        extends AbstractDao<Review> {

    // ========================================================= Behaviour == //
    @Override
    public Review get(Long id)
            throws SQLException {
        connectSQL();

        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM Reviews WHERE ID = ?")) {
            List<Review> reviews = new ArrayList<>();

            statement.setLong(1, id);
            rs = statement.executeQuery();

            GameDAO gameDao = new GameDAO();
            UserDAO userDao = new UserDAO();
            while (rs.next()) {
                reviews.add(new Review(
                        rs.getLong("ID"),
                        gameDao.get(rs.getLong("GameID")),
                        userDao.get(rs.getLong("UserID")),
                        rs.getInt("Rating")
                ));
            }

            return reviews.get(0);

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());

        } finally {
            rs.close();
            disconnectSQL();
        }
    }

    @Override
    public List<Review> getAll()
            throws SQLException {
        connectSQL();

        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM Reviews")) {
            List<Review> reviews = new ArrayList<>();

            rs = statement.executeQuery();

            GameDAO gameDao = new GameDAO();
            UserDAO userDao = new UserDAO();
            while (rs.next()) {
                reviews.add(new Review(
                        rs.getLong("ID"),
                        gameDao.get(rs.getLong("GameID")),
                        userDao.get(rs.getLong("UserID")),
                        rs.getInt("Rating")
                ));
            }

            return reviews;

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());

        } finally {
            rs.close();
            disconnectSQL();
        }
    }

    @Override
    protected void insert(Review review)
            throws SQLException {
        connectSQL();

        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO Reviews(GameID, UserID, Rating) VALUES (?, ?, ?)")) {
            statement.setLong(1, review.getGame().getId());
            statement.setLong(2, review.getUser().getId());
            statement.setInt(3, review.getRating());
            statement.executeUpdate();

            List<Review> reviews = getAll();
            review.setId(reviews.get(reviews.size() - 1).getId());

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());

        } finally {
            disconnectSQL();
        }
    }

    @Override
    protected void update(Review review) throws SQLException {
        // TODO: Write this function
//        connectSQL();
//
//        String query = "UPDATE Opinions SET Reply = '%s' WHERE Opinions.ID = %d";
//
//        query = String.format(query, review.getAuthorsReply(), review.getId());
////        query = query.replace("false", "0");
////        query = query.replace("true", "1");
//
//        System.out.println(query);
//
//        try {
//
//            stmt = conn.createStatement();
//            stmt.executeUpdate(query);
//
//        } catch (SQLException e) {
//
//            throw new SQLException(e.getMessage());
//
//        } finally {
//
//            disconnectSQL();
//        }
    }

    @Override
    protected void delete(Review review)
            throws SQLException {
        // TODO: Write this function
    }
}
