package com.pdidkovskiy.shortlinks.repository;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

interface Repository<K extends Serializable, T> {

    T save(T entity);

    Optional<T> findOne(K id);

}
