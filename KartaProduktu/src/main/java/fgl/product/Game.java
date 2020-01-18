package fgl.product;

public class Game {

    private Long id;
    private Long userId;
    private String title;
    private Integer version;
    private String tags;
    private String genre;
    private String description;
    private Integer userCount;
    private boolean isReported;

    public Game(Long userId, String title, String tags, String genre, String description) {
        this.userId = userId;
        this.title = title;
        this.tags = tags;
        this.genre = genre;
        this.description = description;
    }

    public Game(Long id, Long userId, String title, String tags, String genre, String description, Integer userCount, boolean isReported) {

        this.id = id;
        this.userId = userId;
        this.title = title;
        this.tags = tags;
        this.genre = genre;
        this.description = description;
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

    public String getDescription() {

        return description;
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

    public void setDescription(String description) {

        this.description = description;
    }

    public void setUserCount(Integer userCount) {

        this.userCount = userCount;
    }

    public void setGenre(String genre) {

        this.genre = genre;
    }

    public void setReported(boolean reported) {

        isReported = reported;
    }

    public Integer getVersion()
    {
        return version;
    }

    public void setVersion(Integer version)
    {
        this.version = version;
    }
}
