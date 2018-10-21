package domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;

public class Link {

    @JsonIgnore
    private String id;

    private String shortLink;

    private String originalLink;

    private String userLogin;

    private AtomicInteger hits;

    public Link() {
    }

    public Link(String userLogin, String shortLink, String originalLink) {
        this.id = createId(userLogin, shortLink);
        this.userLogin = userLogin;
        this.shortLink = shortLink;
        this.originalLink = originalLink;
        this.hits = new AtomicInteger();
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

    public AtomicInteger getHits() {
        return hits;
    }

    public void setHits(AtomicInteger hits) {
        this.hits = hits;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public String getId() {
        return id;
    }

}
