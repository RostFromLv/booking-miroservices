package com.example.addressservice.domain;

import com.example.bookingcommonabstractservice.converter.Convertible;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Class that represents an address entity from the real world.
 *
 * @author Roslyslav Balushchak
 * @since 1.0.0-SNAPSHOT
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "address")
public class Address implements Convertible {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column
  private String country;
  @Column
  private String city;
  @Column
  private String street;
  @Column
  private Integer houseNumber;
  @Column
  private String postalCode;
}
