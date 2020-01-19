// ////////////////////////////////////////////////////////////////// Package //
package fgl.kartaocen;

// ////////////////////////////////////////////////////////////////// Imports //
// ================================================================= Other == //
import java.util.Date;

// /////////////////////////////////////////////////////////// Class: Comment //
public class Comment {

    // ============================================================== Data == //
    private Long id;
    private Review review;
    private String content;
    private Date submissionDate;
    private boolean isReply;

    // ========================================================= Behaviour == //
    public Comment(Long id,
                   Review review,
                   String content,
                   Date submissionDate,
                   boolean isReply) {
        this.id = id;
        this.review = review;
        this.content = content;
        this.submissionDate = submissionDate;
        this.isReply = isReply;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = String.copyValueOf(content.toCharArray());
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Date submissionDate) {
        this.submissionDate = submissionDate;
    }

    public boolean isReply() {
        return isReply;
    }

    public void setReply(boolean reply) {
        isReply = reply;
    }
}

// ////////////////////////////////////////////////////////////////////////// //

