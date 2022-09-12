package com.example.bookingcommonabstractservice.converter;

import org.modelmapper.ModelMapper;

public class DtoConverter<E extends Convertible, D extends Convertible> {

  private final ModelMapper modelMapper;

  public DtoConverter(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public <E extends Convertible, D extends Convertible> D convertToDto(
        final E entity,
        final Class<D> dto) {

    return modelMapper.map(entity, dto);
  }

  public <D extends Convertible, E extends Convertible> E convertToEntity(
        final D dto,
        final Class<E> entity) {
    return modelMapper.map(dto, entity);
  }

  public <D extends Convertible, E extends Convertible> void update(final D source, final E target) {
    modelMapper.map(source, target);
  }
}
