package com.example.bookingcommonabstractservice.service;

import com.example.bookingcommonabstractservice.converter.Convertible;
import java.util.Collection;
import java.util.Optional;

public interface DataService<ID, D extends Convertible> {

  Collection<D> findAll();

  Optional<D> findById(final ID id);

  D create(final D target);

  D update(final D target,ID id);

  void deleteById(final ID id);

  boolean contains(final ID id);

  void deleteAll();
}
