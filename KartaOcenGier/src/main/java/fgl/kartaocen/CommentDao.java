// ////////////////////////////////////////////////////////////////// Package //
package fgl.kartaocen;

// ////////////////////////////////////////////////////////////////// Imports //
// =================================================================== FGL == //
import fgl.database.AbstractDao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// ================================================================= Other == //

// //////////////////////////////////////////////////////// Class: CommentDao //
public class CommentDao
        extends AbstractDao<Comment> {

    // ========================================================= Behaviour == //
    @Override
    public Comment get(Long id)
            throws SQLException {
        connectSQL();

        try (PreparedStatement statement = conn
                .prepareStatement("SELECT * FROM Comments WHERE ID = ?")) {
            List<Comment> comments = new ArrayList<>();

            statement.setLong(1, id);
            rs = statement.executeQuery();

            ReviewDao reviewDao = new ReviewDao();
            while (rs.next()) {
                comments.add(new Comment(rs.getLong("ID"),
                                         reviewDao.get(rs.getLong("ReviewID")),
                                         rs.getString("Content"),
                                         rs.getTimestamp("SubmissionDate"),
                                         rs.getBoolean("IsReply")));
            }

            return comments.get(0);

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());

        } finally {
            rs.close();
            disconnectSQL();
        }
    }

    @Override
    public List<Comment> getAll()
            throws SQLException {
        connectSQL();

        try (PreparedStatement statement = conn
                .prepareStatement("SELECT * FROM Comments")) {
            List<Comment> comments = new ArrayList<>();

            rs = statement.executeQuery();

            ReviewDao reviewDao = new ReviewDao();
            while (rs.next()) {
                comments.add(new Comment(rs.getLong("ID"),
                                         reviewDao.get(rs.getLong("ReviewID")),
                                         rs.getString("Content"),
                                         rs.getTimestamp("SubmissionDate"),
                                         rs.getBoolean("IsReply")));
            }

            return comments;

        } catch (SQLException e) {
            throw new SQLException(e.getMessage());

        } finally {
            rs.close();
            disconnectSQL();
        }
    }

    @Override
    protected void insert(Comment comment)
            throws SQLException {
        connectSQL();

        try (PreparedStatement statement = conn.prepareStatement(
                "INSERT INTO Comments(ReviewID, Content, IsReply) VALUES (?, ?, ?)")) {
            statement.setLong(1, comment.getReview().getId());
            statement.setString(2, comment.getContent());
            statement.setBoolean(3, comment.isReply());
            statement.executeUpdate();

            List<Comment> comments = getAll();
            comment.setId(comments.get(comments.size() - 1).getId());
            comment.setSubmissionDate(
                    comments.get(comments.size() - 1).getSubmissionDate());

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
    }

    @Override
    protected void delete(Comment comment)
            throws SQLException {
        // TODO: Write this function
    }
}

// ////////////////////////////////////////////////////////////////////////// //
