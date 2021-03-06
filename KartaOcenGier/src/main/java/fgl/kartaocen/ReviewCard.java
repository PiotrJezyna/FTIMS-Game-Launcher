// ////////////////////////////////////////////////////////////////// Package //
package fgl.kartaocen;

// ////////////////////////////////////////////////////////////////// Imports //
// ================================================================ JavaFX == //
import fgl.communication.MailHandler;
import fgl.product.Game;
import fgl.product.GameDAO;
import fgl.userPanel.User;
import fgl.userPanel.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;

import java.sql.SQLException;
import java.util.*;

// //////////////////////////////////////////////////////// Class: ReviewCard //
public class ReviewCard {

    // ============================================================== Data == //
    static private ReviewDao  reviewDao  = new ReviewDao();
    static private CommentDao commentDao = new CommentDao();

    private User loggedUser;
    private Game game;

    private int rating = 10;

    private boolean showReviewEditHistory = false;
    private boolean showReplyEditHistory  = false;

    private List<Review>  currentGameReviews  = new ArrayList<>();
    private List<Comment> currentGameComments = new ArrayList<>();

    private ListIterator<Review>  currentGameReviewIterator;
    private ListIterator<Comment> currentGamesCurrentCommentIterator;
    private ListIterator<Comment> currentGamesCurrentReplyIterator;

    private Map<Review, List<Comment>> userCommentsPerReview  = new HashMap<>();
    private Map<Review, List<Comment>> authorRepliesPerReview = new HashMap<>();

    @FXML private Label    labelAverageRating;
    @FXML private Label    labelGameTitle;
    @FXML private Label    labelReviewUsername;
    @FXML private Label    labelReviewDate;
    @FXML private Label    labelReplyUsername;
    @FXML private Label    labelReplyDate;
    @FXML private TextArea textAreaReview;
    @FXML private TextArea textAreaReply;
    @FXML private Button   buttonRating1;
    @FXML private Button   buttonRating2;
    @FXML private Button   buttonRating3;
    @FXML private Button   buttonRating4;
    @FXML private Button   buttonRating5;
    @FXML private Button   buttonRating6;
    @FXML private Button   buttonRating7;
    @FXML private Button   buttonRating8;
    @FXML private Button   buttonRating9;
    @FXML private Button   buttonRating10;

    private       List<Button> ratingButtons;
    @FXML private Button       buttonShowReviewEditHistory;
    @FXML private Button       buttonReviewPreviousEdit;
    @FXML private Button       buttonReviewNextEdit;
    @FXML private Button       buttonShowReplyEditHistory;
    @FXML private Button       buttonReplyPreviousEdit;
    @FXML private Button       buttonReplyNextEdit;
    @FXML private Button       buttonGoBackToReview;
    @FXML private Slider       sliderReviews;
    @FXML private Button       buttonSubmitReview;
    @FXML private Button       buttonSubmitReply;
    @FXML private GridPane     mainGrid;

    // ========================================================= Behaviour == //
    public ReviewCard() {
    }

    @FXML
    private void addReview()
            throws SQLException {
        Review review = new Review(0L, game, loggedUser, rating);
        Comment comment = new Comment(0L, review,
                                      textAreaReview.getText() == null ? "" :
                                              textAreaReview.getText(), null,
                                      false);

        if (!hasUserReviewedBefore()) {
            reviewDao.insert(review);
            currentGameReviews.add(review);
            resetReviewIterator();
        } else {
            review.setId(getCurrentUserReview().getId());
            reviewDao.update(review);
            getCurrentUserReview().setRating(review.getRating());
        }

        Comment currentComment = getCurrentGamesCurrentComment();
        if ((currentComment == null && !comment.getContent().isEmpty()) ||
            (currentComment != null &&
             !currentComment.getContent().equals(comment.getContent()))) {
            commentDao.insert(comment);
            currentGameComments.add(comment);
        }

        getCurrentGamesReviews();
        getCurrentGamesComments();
        organizeUserCommentsAndAuthorsRepliesPerReview();
        resetReviewIterator();
        resetCommentIterators();

        updateUserInterface();

        notifyAuthor();
    }

    private Review getCurrentUserReview() {
        for (Review review : currentGameReviews) {
            if (loggedUser.getId().equals(review.getUser().getId())) {
                return review;
            }
        }
        return null;
    }

    private void notifyAuthor()
            throws SQLException {
        UserDAO userDAO = new UserDAO();
        User    user    = userDAO.get(game.getUserId());
        MailHandler.sendMail(user.getUsername(), user.getEmail(), "review added");
    }

    @FXML
    private void authorsReply()
            throws SQLException {
        Review review = getCurrentGameReview();
        Comment comment = new Comment(0L, review,
                                      textAreaReply.getText() == null ? "" :
                                              textAreaReply.getText(), null,
                                      true);

        Comment currentComment = getCurrentGamesCurrentReply();
        if ((currentComment == null && !comment.getContent().isEmpty()) ||
            (currentComment != null &&
             !currentComment.getContent().equals(comment.getContent()))) {
            commentDao.insert(comment);
            currentGameComments.add(comment);
        }

        getCurrentGamesComments();
        organizeUserCommentsAndAuthorsRepliesPerReview();
        resetCommentIterators();

        updateUserInterface();
    }

    @FXML
    private void chooseReviewToShow() {
        if ((int) Math.round(sliderReviews.getValue()) - 1 >= 0) {
            currentGameReviewIterator = currentGameReviews.listIterator(
                    (int) Math.round(sliderReviews.getValue()) - 1);
        } else {
            currentGameReviewIterator = currentGameReviews.listIterator(0);
        }
        resetCommentIterators();
        showReviewEditHistory = false;
        showReplyEditHistory = false;
        rating = getCurrentGameReview().getRating();

        updateUserInterface();
    }

    @FXML
    private void goBackToUsersReview() {
        resetReviewIterator();
        resetCommentIterators();
        showReviewEditHistory = false;
        showReplyEditHistory = false;

        updateUserInterface();
    }

    @FXML
    private void hover1() {
        hover(1);
    }

    @FXML
    private void hover10() {
        hover(10);
    }

    @FXML
    private void hover2() {
        hover(2);
    }

    @FXML
    private void hover3() {
        hover(3);
    }

    @FXML
    private void hover4() {
        hover(4);
    }

    @FXML
    private void hover5() {
        hover(5);
    }

    @FXML
    private void hover6() {
        hover(6);
    }

    @FXML
    private void hover7() {
        hover(7);
    }

    @FXML
    private void hover8() {
        hover(8);
    }

    @FXML
    private void hover9() {
        hover(9);
    }

    public void init()
            throws SQLException {
        getCurrentGamesReviews();
        getCurrentGamesComments();
        organizeUserCommentsAndAuthorsRepliesPerReview();
        resetReviewIterator();
        resetCommentIterators();

        rating = getCurrentGameReview() == null ? 10 :
                getCurrentGameReview().getRating();

        // Create button list
        ratingButtons = new ArrayList<>();
        ratingButtons.add(buttonRating1);
        ratingButtons.add(buttonRating2);
        ratingButtons.add(buttonRating3);
        ratingButtons.add(buttonRating4);
        ratingButtons.add(buttonRating5);
        ratingButtons.add(buttonRating6);
        ratingButtons.add(buttonRating7);
        ratingButtons.add(buttonRating8);
        ratingButtons.add(buttonRating9);
        ratingButtons.add(buttonRating10);

        updateUserInterface();
    }

    private void getCurrentGamesReviews()
            throws SQLException {
        currentGameReviews.clear();

        List<Review> allReviews = reviewDao.getAll();
        for (Review review : allReviews) {
            if (review.getGame().getId().equals(game.getId())) {
                currentGameReviews.add(review);
            }
        }

        if (!isUserAuthor() && !hasUserReviewedBefore()) {
            currentGameReviews.add(currentGameReviews.size(),
                                   new Review(game, loggedUser, rating));
        }
    }

    private void getCurrentGamesComments()
            throws SQLException {
        currentGameComments.clear();

        List<Comment> allComments = commentDao.getAll();
        for (Comment comment : allComments) {
            if (comment.getReview().getGame().getId().equals(game.getId())) {
                currentGameComments.add(comment);
            }
        }
    }

    private void organizeUserCommentsAndAuthorsRepliesPerReview() {
        for (Review review : currentGameReviews) {
            userCommentsPerReview.put(review, new ArrayList<>());
            authorRepliesPerReview.put(review, new ArrayList<>());

            for (Comment comment : currentGameComments) {
                if (!review.getId().equals(comment.getReview().getId())) {
                    continue;
                }

                if (comment.isReply()) {
                    authorRepliesPerReview.get(review).add(comment);
                } else {
                    userCommentsPerReview.get(review).add(comment);
                }
            }
        }
    }

    private void resetReviewIterator() {
        currentGameReviewIterator = currentGameReviews.listIterator();
        if (isUserAuthor()) {
            return;
        }
        while (currentGameReviewIterator.hasNext()) {
            if (getCurrentGameReview().getUser().getId()
                                      .equals(loggedUser.getId())) {
                break;
            }
            currentGameReviewIterator.next();
        }
    }

    private void resetCommentIterators() {
        if (getCurrentGameReview() != null) {
            currentGamesCurrentCommentIterator = userCommentsPerReview
                    .get(getCurrentGameReview()).listIterator(
                            userCommentsPerReview.get(getCurrentGameReview())
                                                 .isEmpty() ? 0 :
                                    userCommentsPerReview
                                            .get(getCurrentGameReview())
                                            .size() - 1);

            currentGamesCurrentReplyIterator = authorRepliesPerReview
                    .get(getCurrentGameReview()).listIterator(
                            authorRepliesPerReview.get(getCurrentGameReview())
                                                  .isEmpty() ? 0 :
                                    authorRepliesPerReview
                                            .get(getCurrentGameReview())
                                            .size() - 1);
        } else {
            currentGamesCurrentCommentIterator = null;
            currentGamesCurrentReplyIterator = null;
        }
    }

    private Review getCurrentGameReview() {
        if (currentGameReviewIterator != null) {
            if (currentGameReviewIterator.nextIndex() !=
                currentGameReviews.size()) {
                Review review = currentGameReviewIterator.next();
                currentGameReviewIterator.previous();
                return review;
            } else if (currentGameReviewIterator.hasPrevious()) {
                Review review = currentGameReviewIterator.previous();
                currentGameReviewIterator.next();
                return review;
            }
        }
        return null;
    }

    private void updateUserInterface() {
        // .................................................................. //
        // Set game title
        labelGameTitle.setText(game.getTitle());

        // .................................................................. //
        // Update average rating information
        if (getRealNumberOfCurrentGameReviews() > 0) {
            int size = getRealNumberOfCurrentGameReviews();
            labelAverageRating.setText(
                    (int) getAverageRating(game) + " / 10 (based on " + size +
                    " review" + (size > 1 ? "s" : "") + ")");
        } else {
            labelAverageRating.setText("There are no reviews");
        }

        // .................................................................. //
        // Show author's reply only if exists
        if (getCurrentGameReview() != null) {
            setRowVisibility(mainGrid, 4, isUserAuthor() ||
                                          !authorRepliesPerReview
                                                  .get(getCurrentGameReview())
                                                  .isEmpty());
        } else {
            setRowVisibility(mainGrid, 4, isUserAuthor());
        }

        // .................................................................. //
        // Show reviews' slider only if needed
        if (isUserAuthor()) {
            setRowVisibility(mainGrid, 5,
                             getRealNumberOfCurrentGameReviews() > 1);
            setRowVisibility(mainGrid, 6,
                             getRealNumberOfCurrentGameReviews() > 1);
        } else if (getRealNumberOfCurrentGameReviews() == 0) {
            setRowVisibility(mainGrid, 5, false);
            setRowVisibility(mainGrid, 6, false);
        } else if (getRealNumberOfCurrentGameReviews() > 1 ||
                   !hasUserReviewedBefore()) {
            setRowVisibility(mainGrid, 5, true);
            setRowVisibility(mainGrid, 6, true);
        } else {
            setRowVisibility(mainGrid, 5, false);
            setRowVisibility(mainGrid, 6, false);
        }

        // .................................................................. //
        // Set username information
        if (doesCurrentGameReviewBelongToLoggedUser()) {
            labelReviewUsername.setText("/ Your review");
        } else {
            if (getCurrentGameReview() != null) {
                labelReviewUsername.setText(
                        "/ " + getCurrentGameReview().getUser().getUsername() +
                        "'s review");
            } else {
                labelReviewUsername.setText("/ Your review");
            }
        }

        if (isUserAuthor()) {
            labelReplyUsername.setText("/ Your reply");
        } else {
            labelReplyUsername.setText("/ Author's reply");
        }

        // .................................................................. //
        // Enable review's rating buttons if needed
        for (Button button : ratingButtons) {
            button.setDisable(!doesCurrentGameReviewBelongToLoggedUser());
        }

        // .................................................................. //
        // Set appropriate star rating
        for (int i = 0; i < ratingButtons.size(); ++i) {
            if (i < rating) {
                ratingButtons.get(i).setText(Character.toString('\u2605'));
            } else {
                ratingButtons.get(i).setText(Character.toString('\u2606'));
            }
        }

        // .................................................................. //
        // Set comment's content
        textAreaReview.setText(getCurrentGamesCurrentComment() == null ? "" :
                                       getCurrentGamesCurrentComment()
                                               .getContent());
        textAreaReply.setText(getCurrentGamesCurrentReply() == null ? "" :
                                      getCurrentGamesCurrentReply()
                                              .getContent());

        // .................................................................. //
        // Disable comment's content if showing edit history
        textAreaReview.setEditable(!showReviewEditHistory && !isUserAuthor());
        textAreaReply.setEditable(!showReplyEditHistory && isUserAuthor());

        // .................................................................. //
        // Set comment's submission date
        labelReviewDate.setText(getCurrentGamesCurrentComment() == null ? "" :
                                        getCurrentGamesCurrentComment()
                                                .getSubmissionDate().
                                                toString());
        labelReplyDate.setText(getCurrentGamesCurrentReply() == null ? "" :
                                       getCurrentGamesCurrentReply()
                                               .getSubmissionDate().
                                               toString());

        // .................................................................. //
        // Set edit history buttons' content
        buttonShowReviewEditHistory.setVisible(numberOfReviewEdits() > 1);
        buttonShowReplyEditHistory.setVisible(numberOfReplyEdits() > 1);

        buttonShowReviewEditHistory.setText(
                (showReviewEditHistory ? "Hide" : "Show") + " edit history");
        buttonShowReplyEditHistory.setText(
                (showReplyEditHistory ? "Hide" : "Show") + " edit history");

        buttonReviewPreviousEdit.setVisible(showReviewEditHistory);
        buttonReviewNextEdit.setVisible(showReviewEditHistory);
        buttonReplyPreviousEdit.setVisible(showReplyEditHistory);
        buttonReplyNextEdit.setVisible(showReplyEditHistory);

        if (currentGamesCurrentCommentIterator != null) {
            buttonReviewPreviousEdit.setDisable(
                    !currentGamesCurrentCommentIterator.hasPrevious());
        } else {
            buttonReviewPreviousEdit.setDisable(true);
        }
        if (getCurrentGameReview() != null) {
            buttonReviewNextEdit.setDisable(
                    currentGamesCurrentCommentIterator.nextIndex() ==
                    Math.max(0,
                             userCommentsPerReview.get(getCurrentGameReview())
                                                  .size() - 1));
            buttonReplyNextEdit.setDisable(
                    currentGamesCurrentReplyIterator.nextIndex() == Math.max(0,
                                                                             authorRepliesPerReview
                                                                                     .get(getCurrentGameReview())
                                                                                     .size() -
                                                                             1));
        } else {
            buttonReviewNextEdit.setDisable(true);
            buttonReplyNextEdit.setDisable(true);
        }
        if (currentGamesCurrentReplyIterator != null) {
            buttonReplyPreviousEdit.setDisable(
                    !currentGamesCurrentReplyIterator.hasPrevious());
        } else {
            buttonReplyPreviousEdit.setDisable(true);
        }

        // .................................................................. //
        // Set submission button's visibility
        if (getCurrentGameReview() != null) {
            buttonSubmitReview.setVisible(!showReviewEditHistory &&
                                          getCurrentGameReview().getUser()
                                                                .getId()
                                                                .equals(loggedUser
                                                                                .getId()));
        } else {
            buttonSubmitReview.setVisible(!showReviewEditHistory);
        }

        buttonSubmitReply.setVisible(!showReplyEditHistory && isUserAuthor());

        // .................................................................. //
        // Set reviews' slider
        sliderReviews.setMin(1);
        sliderReviews.setMax(currentGameReviews.size());
        sliderReviews.setValue(currentGameReviewIterator.nextIndex() + 1);

        // .................................................................. //
        // Author's things
        buttonGoBackToReview.setVisible(!isUserAuthor());

        if (getRealNumberOfCurrentGameReviews() == 0 && isUserAuthor()) {
            setRowVisibility(mainGrid, 2, false);
            setRowVisibility(mainGrid, 3, false);
            setRowVisibility(mainGrid, 4, false);
            setRowVisibility(mainGrid, 5, false);
            setRowVisibility(mainGrid, 6, false);
        }
    }

    private boolean isUserAuthor() {
        return loggedUser.getId().equals(game.getUserId());
    }

    private boolean hasUserReviewedBefore() {
        for (Review review : currentGameReviews) {
            if (loggedUser.getId().equals(review.getUser().getId()) &&
                review.getId() != 0) {
                return true;
            }
        }
        return false;
    }

    private int getRealNumberOfCurrentGameReviews() {
        int result = currentGameReviews.size();
        if (!isUserAuthor() && !hasUserReviewedBefore()) {
            result--;
        }
        return result;
    }

    public double getAverageRating(Game game) {
        int     sumOfRatings = 0;
        boolean newReview    = false;
        for (Review review : currentGameReviews) {
            if (!review.getId().equals(0L)) {
                sumOfRatings += review.getRating();
            } else {
                newReview = true;
            }
        }
        return ((double) sumOfRatings) /
               (currentGameReviews.size() - (newReview ? 1 : 0));
    }

    static private void setRowVisibility(GridPane grid, int row,
                                         boolean value) {
        for (Node child : grid.getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(child);

            if (((rowIndex == null) ? 0 : rowIndex) == row) {
                child.setVisible(value);
            }
        }
    }

    private boolean doesCurrentGameReviewBelongToLoggedUser() {
        if (getCurrentGameReview() != null) {
            return getCurrentGameReview().getUser().getId()
                                         .equals(loggedUser.getId());
        } else {
            return true;
        }
    }

    private Comment getCurrentGamesCurrentComment() {
        if (getCurrentGameReview() != null) {
            if (currentGamesCurrentCommentIterator.nextIndex() !=
                userCommentsPerReview.get(getCurrentGameReview()).size()) {
                Comment comment = currentGamesCurrentCommentIterator.next();
                currentGamesCurrentCommentIterator.previous();
                return comment;
            } else if (currentGamesCurrentCommentIterator.hasPrevious()) {
                Comment comment = currentGamesCurrentCommentIterator.previous();
                currentGamesCurrentCommentIterator.next();
                return comment;
            }
        }
        return null;
    }

    private Comment getCurrentGamesCurrentReply() {
        if (getCurrentGameReview() != null) {
            if (currentGamesCurrentReplyIterator.nextIndex() !=
                authorRepliesPerReview.get(getCurrentGameReview()).size()) {
                Comment comment = currentGamesCurrentReplyIterator.next();
                currentGamesCurrentReplyIterator.previous();
                return comment;
            } else if (currentGamesCurrentReplyIterator.hasPrevious()) {
                Comment comment = currentGamesCurrentReplyIterator.previous();
                currentGamesCurrentReplyIterator.next();
                return comment;
            }
        }
        return null;
    }

    private int numberOfReviewEdits() {
        if (getCurrentGameReview() != null) {
            return userCommentsPerReview.get(getCurrentGameReview()).size();
        }
        return 0;
    }

    private int numberOfReplyEdits() {
        if (getCurrentGamesCurrentReply() != null) {
            return authorRepliesPerReview.get(getCurrentGameReview()).size();
        }
        return 0;
    }

    @FXML
    private void replyNextEdit() {
        if (currentGamesCurrentReplyIterator.nextIndex() !=
            authorRepliesPerReview.get(getCurrentGameReview()).size() - 1) {
            currentGamesCurrentReplyIterator.next();
        }

        updateUserInterface();
    }

    @FXML
    private void replyPreviousEdit() {
        if (currentGamesCurrentReplyIterator.hasPrevious()) {
            currentGamesCurrentReplyIterator.previous();
        }

        updateUserInterface();
    }

    @FXML
    private void reviewNextEdit() {
        if (currentGamesCurrentCommentIterator.nextIndex() !=
            userCommentsPerReview.get(getCurrentGameReview()).size() - 1) {
            currentGamesCurrentCommentIterator.next();
        }

        updateUserInterface();
    }

    @FXML
    private void reviewPreviousEdit() {
        if (currentGamesCurrentCommentIterator.hasPrevious()) {
            currentGamesCurrentCommentIterator.previous();
        }

        updateUserInterface();
    }

    public void setGame(Long id) {
        try {
            game = (new GameDAO()).get(id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setLoggedUser(Long id) {
        try {
            loggedUser = (new UserDAO()).get(id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void setRating1() {
        setRating(1);
    }

    private void setRating(int value) {
        rating = value;

        updateUserInterface();
    }

    @FXML
    private void setRating10() {
        setRating(10);
    }

    @FXML
    private void setRating2() {
        setRating(2);
    }

    @FXML
    private void setRating3() {
        setRating(3);
    }

    @FXML
    private void setRating4() {
        setRating(4);
    }

    @FXML
    private void setRating5() {
        setRating(5);
    }

    @FXML
    private void setRating6() {
        setRating(6);
    }

    @FXML
    private void setRating7() {
        setRating(7);
    }

    @FXML
    private void setRating8() {
        setRating(8);
    }

    @FXML
    private void setRating9() {
        setRating(9);
    }

    @FXML
    private void showReplyEditHistory() {
        showReplyEditHistory = !showReplyEditHistory;

        updateUserInterface();
    }

    @FXML
    private void showReviewEditHistory() {
        showReviewEditHistory = !showReviewEditHistory;

        updateUserInterface();
    }

    @FXML
    private void unhover1() {
        unhover();
    }

    private void unhover() {
        hover(rating);

        String tempContent = textAreaReview.getText();
        updateUserInterface();
        textAreaReview.setText(tempContent == null ? "" : tempContent);
    }

    private void hover(int stars) {
        for (int i = 0; i < ratingButtons.size(); i++) {
            if (i < stars) {
                ratingButtons.get(i).setText(Character.toString('\u2605'));
            } else {
                ratingButtons.get(i).setText(Character.toString('\u2606'));
            }
        }
    }

    @FXML
    private void unhover10() {
        unhover();
    }

    @FXML
    private void unhover2() {
        unhover();
    }

    @FXML
    private void unhover3() {
        unhover();
    }

    @FXML
    private void unhover4() {
        unhover();
    }

    @FXML
    private void unhover5() {
        unhover();
    }

    @FXML
    private void unhover6() {
        unhover();
    }

    @FXML
    private void unhover7() {
        unhover();
    }

    @FXML
    private void unhover8() {
        unhover();
    }

    @FXML
    private void unhover9() {
        unhover();
    }
}

// ////////////////////////////////////////////////////////////////////////// //

