package com.example.bookingcommonabstractservice.service;

import com.example.bookingcommonabstractservice.converter.Convertible;
import com.example.bookingcommonabstractservice.converter.DtoConverter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Default abstract service for all models.
 *
 * @param <Id> - id type of our entity
 * @param <E>  - entity type for our abstract service
 * @param <D>  - Dto type for our entity
 */
public abstract class AbstractDataService<@NotNull Id,@NotNull E extends Convertible,@NotNull D extends Convertible>
    implements DataService<Id, D> {

  private final static String idCannotBeNull = "Id cannot be null";
  private final static String entityNotFound = "Entity with id %s doesnt exist";

  protected final DtoConverter<E, D> converter = new DtoConverter(new ModelMapper());

  protected JpaRepository<E, Id> repository;

  private final Class<D> dtoType;

  private final Class<E> entityType;

  @SuppressWarnings("unchecked")
  public AbstractDataService() {
    Type superClass = getClass().getGenericSuperclass();
    Type[] typeArguments = ((ParameterizedType) superClass).getActualTypeArguments();
    this.dtoType = (Class<D>) typeArguments[2];
    this.entityType = (Class<E>) typeArguments[1];
  }

  @Override
  @Transactional(readOnly = true)
  public Collection<D> findAll() {
    return repository.findAll()
        .stream()
        .map(entity -> this.converter.convertToDto(entity, this.dtoType))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<D> findById(Id id) {
    Assert.notNull(id, idCannotBeNull);
    return repository.findById(id)
        .map(entity -> converter.convertToDto(entity, dtoType.asSubclass(dtoType)));
  }

  @Override
  @Transactional
  public D create(@NotNull D target) {
    Assert.notNull(target , "Dto" + target.getClass() + " cannot be null");
    E createdEntity = repository.save(converter.convertToEntity(target,
        entityType.asSubclass(entityType.asSubclass(entityType))));
    return converter.convertToDto(createdEntity, dtoType.asSubclass(dtoType));
  }

  @Override
  @Transactional
  public D update(@NotNull D target, Id id) {
    Assert.notNull(target, target.getClass() + " cant be null");
    Assert.notNull(id, idCannotBeNull);
    E existEntity = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(String.format(entityNotFound, id)));

    converter.update(target, existEntity);

    E updatedEntity = repository.save(existEntity);

    return converter.convertToDto(updatedEntity, dtoType.asSubclass(dtoType));
  }

  @Override
  @Transactional
  public void deleteById(Id id) {
    Assert.notNull(id, idCannotBeNull);
    repository.deleteById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean contains(Id id) {
    Assert.notNull(id, idCannotBeNull);
    return repository.existsById(id);
  }

  @Override
  @Transactional
  public void deleteAll() {
    repository.deleteAll();
  }

  @Autowired
  public final void setRepository(JpaRepository<E, Id> repository) {
    this.repository = repository;
  }
}
