package com.pdidkovskiy.shortlinks.repository;

import domain.Link;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class InMemoryLinkRepository implements LinkRepository {

    private static final Map<String, Link> linkStorage = new ConcurrentHashMap<>();

    @Override
    public Link save(Link link) {
        return linkStorage.computeIfAbsent(link.getId(), k -> link);
    }

    @Override
    public Optional<Link> findOne(String id) {
        return Optional.ofNullable(linkStorage.get(id));
    }

    @Override
    public Collection<Link> findAll() {
        return linkStorage.values();
    }

    @Override
    public List<Link> findUserLinks(String login) {
        return linkStorage.values()
                .stream()
                .filter(l -> l.getUserLogin().equals(login))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        linkStorage.remove(id);
    }


}
