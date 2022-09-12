package com.example.addressservice.model;

/**
 * The interface for grouping {@link AddressDto}.
 * <p></p>
 * Use this interface to split validation  requiring for DTO
 *
 * @author Rostyslav Balushchak
 * @since 1.0.0-SNAPSHOT
 */
public interface Groups {
  /**
   * Use this interface for highlight that DTO use like Creating DTO.
   */
  interface Create {
  }

  /**
   * Use this interface for highlight that DTO use like Updating DTO.
   */
  interface Update {
  }
}
