package fgl.product;

public class Game {

    private Long id;
    private Long userId;
    private String title;
    private String tags;
    private String path;
    private Integer userCount;
    private boolean isReported;

    public Game(Long userId, String title, String tags, String path) {
        this.userId = userId;
        this.title = title;
        this.tags = tags;
        this.path = path;
    }

    public Game(Long id, Long userId, String title, String tags, String path, Integer userCount, boolean isReported) {

        this.id = id;
        this.userId = userId;
        this.title = title;
        this.tags = tags;
        this.path = path;
        this.userCount = userCount;
        this.isReported = isReported;
    }

    public Long getId() {

        return id;
    }

    public Long getUserId() {

        return userId;
    }

    public String getTitle() {

        return title;
    }

    public String getTags() {

        return tags;
    }

    public String getPath() {

        return path;
    }

    public Integer getUserCount() {

        return userCount;
    }

    public boolean isReported() {

        return isReported;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTags(String tags) {

        this.tags = tags;
    }

    public void setPath(String path) {

        this.path = path;
    }

    public void setUserCount(Integer userCount) {

        this.userCount = userCount;
    }

    public void setReported(boolean reported) {

        isReported = reported;
    }
}
