// ////////////////////////////////////////////////////////////////// Package //
package fgl.kartaocen;

// ////////////////////////////////////////////////////////////////// Imports //
// =================================================================== FGL == //
import fgl.database.AbstractDao;
import fgl.product.GameDAO;
import fgl.userPanel.UserDAO;

// ================================================================= Other == //
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

// //////////////////////////////////////////////////////// Class: CommentDao //
public class CommentDao
        extends AbstractDao<Comment> {

    // ========================================================= Behaviour == //
    @Override
    public Comment get(Long id)
            throws SQLException {
        connectSQL();

        try {
            List<Comment> comments = new ArrayList<>();

            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Comments WHERE ID = ?");
            statement.setLong(1, id);
            rs = statement.executeQuery();

            ReviewDao reviewDao = new ReviewDao();
            while (rs.next()) {
                comments.add(
                        new Comment(
                                rs.getLong("ID"),
                                reviewDao.get(rs.getLong("ReviewID")),
                                rs.getString("Content"),
                                rs.getDate("SubmissionDate"),
                                rs.getBoolean("IsReply")));
            }

            return comments.get(0);

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());

        } finally {
            rs.close();
            stmt.close();
            disconnectSQL();
        }
    }

    @Override
    public List<Comment> getAll()
            throws SQLException {
        connectSQL();

        try {
            List<Comment> comments = new ArrayList<>();

            PreparedStatement statement = conn.prepareStatement("SELECT * FROM Comments");
            rs = statement.executeQuery();

            ReviewDao reviewDao = new ReviewDao();
            while (rs.next()) {
                comments.add(
                        new Comment(
                                rs.getLong("ID"),
                                reviewDao.get(rs.getLong("ReviewID")),
                                rs.getString("Content"),
                                rs.getDate("SubmissionDate"),
                                rs.getBoolean("IsReply")));
            }

            return comments;

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());

        } finally {
            rs.close();
            stmt.close();
            disconnectSQL();
        }
    }

    @Override
    protected void insert(Comment comment)
            throws SQLException {
        connectSQL();

        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO Comments(ReviewID, Content, SubmissionDate, IsReply) VALUES (?, ?, ?, ?)");
            statement.setLong(1, comment.getReview().getId());
            statement.setString(2, comment.getContent());
            statement.setDate(3, (Date) comment.getSubmissionDate());
            statement.setBoolean(4, comment.isReply());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());

        } finally {
            disconnectSQL();
        }
    }

    @Override
    protected void update(Comment comment)
            throws SQLException {
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
    protected void delete(Comment comment)
            throws SQLException {
        // TODO: Write this function
    }
}
