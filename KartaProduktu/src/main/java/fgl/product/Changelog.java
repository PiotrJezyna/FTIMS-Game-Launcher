package fgl.product;

import java.util.Date;

public class Changelog {

    private Long ID;
    private Long gameId;
    private String gameTitle;
    private Long version;
    private String description;
    private Date date;

    public Long getID() {
        return ID;
    }

    public Long getGameId() {
        return gameId;
    }

    public Long getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public Date getDate() {
        return date;
    }

    public String getGameTitle() { return gameTitle; }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setGameTitle(String gameTitle) { this.gameTitle = gameTitle; }

    public Changelog(Long ID, Long gameId, Long version, String description, Date date, String gameTitle) {
        this.ID = ID;
        this.gameId = gameId;
        this.version = version;
        this.description = description;
        this.date = date;
        this.gameTitle = gameTitle;
    }
}
