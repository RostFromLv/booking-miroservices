package com.example.commondto.common;

/**
 * Use this interface to mark validation group for Dto.
 *
 * @author Rostyslav
 * @since 1.0.0-SNAPSHOT
 */
public interface Groups {
  /**
   * Use this interface for mark validation for Dto as creating Dto.
   * </p>
   * Creation dto should have not blank string fields.
   * Other object fields should be not null.
   * Id field not need.
   */
  interface Create {
  }

  /**
   * Use this interface for mark update validation for Dto as update Dto.
   * </p>
   * Update dto should have all field.
   * All fields should be not null.
   * String fields should be not blank.
   */
  interface Update {
  }
}
