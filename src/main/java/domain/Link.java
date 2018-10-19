package domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

public class Link {

    @JsonIgnore
    private String id;

    private String shortLink;

    private String originalLink;

    private String userLogin;

    private Integer hits;

    public Link() {
    }

    public Link(String userLogin, String shortLink, String originalLink) {
        this.id = createId(userLogin, shortLink);
        this.userLogin = userLogin;
        this.shortLink = shortLink;
        this.originalLink = originalLink;
        this.hits = 0;
    }

    public static String createId(String userLogin, String shortLink) {
        return StringUtils.join(userLogin, shortLink);
    }

    public String getShortLink() {
        return shortLink;
    }

    public String getOriginalLink() {
        return originalLink;
    }

    public Integer getHits() {
        return hits;
    }

    public void setHits(Integer hits) {
        this.hits = hits;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getId() {
        return id;
    }

}
