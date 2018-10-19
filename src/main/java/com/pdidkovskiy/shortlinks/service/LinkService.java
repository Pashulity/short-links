package com.pdidkovskiy.shortlinks.service;

import com.google.common.hash.Hashing;
import com.pdidkovskiy.shortlinks.exception.BadRequestException;
import com.pdidkovskiy.shortlinks.exception.InvalidUrlException;
import com.pdidkovskiy.shortlinks.repository.InMemoryLinkRepository;
import domain.Link;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static domain.Link.*;

@Service
public class LinkService {

    @Autowired
    private InMemoryLinkRepository userInMemoryLinkRepository;

    @Autowired
    private UserService userService;

    @Value("${service.url}")
    private String serviceUrl;

    private static final Pattern URL_PATTERN =
            Pattern.compile("^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$");

    @PreAuthorize("hasAnyRole('ADMIN, USER')")
    public Link create(String url) {
        validateUrl(url);
        Link link = createLink(url);
        Optional<Link> linkOptional = userInMemoryLinkRepository.findOne(link.getId());
        return linkOptional.orElseGet(() -> userInMemoryLinkRepository.save(link));
    }

    @PreAuthorize("hasAnyRole('ADMIN, USER')")
    public List<Link> getUserLinks() {
        String login = userService.getCurrentUserLogin();
        return userInMemoryLinkRepository.findUserLinks(login);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Collection<Link> getAllLinks() {
        return userInMemoryLinkRepository.findAll();
    }

    @PreAuthorize("hasAnyRole('ADMIN, USER')")
    public Link getLink(String shortLink) {
        String id = createId(userService.getCurrentUserLogin(), shortLink);
        return userInMemoryLinkRepository.findOne(id).orElseThrow(
                () -> new BadRequestException("Short link not found!"));
    }

    @PreAuthorize("hasAnyRole('ADMIN, USER')")
    public void deleteLink(String shortLink, String userLogin) {
        String id = createId(userLogin, shortLink);
        userInMemoryLinkRepository.delete(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN, USER')")
    public String hitLink(String shortLink) {
        String login = userService.getCurrentUserLogin();
        String id = createId(login, shortLink);
        synchronized (id.intern()) {
            Link link = userInMemoryLinkRepository.findOne(id).orElseThrow(
                    () -> new BadRequestException("Short link not found!")
            );
            link.setHits(link.getHits() + 1);
            return link.getOriginalLink();
        }
    }

    private void validateUrl(String link) {
        if (!URL_PATTERN.matcher(link).matches()) {
            throw new InvalidUrlException("Given url is invalid!");
        }
    }

    private Link createLink(String url) {
        String shortLink = StringUtils.join(serviceUrl,
                Hashing.murmur3_32().hashString(url, StandardCharsets.UTF_8).toString());
        return new Link(userService.getCurrentUserLogin(), shortLink, url);
    }

}
