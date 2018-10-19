package com.pdidkovskiy.shortlinks.repository;

import domain.Link;

import java.util.Collection;
import java.util.List;

public interface LinkRepository extends Repository<String, Link> {

    Collection<Link> findAll();

    void delete(String id);

    List<Link> findUserLinks(String login);
}
