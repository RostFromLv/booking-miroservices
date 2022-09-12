package com.example.hotelsservice.domain;

import com.example.bookingcommonabstractservice.converter.Convertible;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

/**
 * The class represent hotel entity in booking system.
 */
@Data
@With
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hotels")
public class Hotel implements Convertible {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column
  private String name;

  @Column(name = "address_id",unique = true)
  private Integer addressId;

  @OneToMany(mappedBy = "hotel",cascade = {CascadeType.REMOVE})
  @JsonManagedReference
  private List<HotelRoom>  rooms = new ArrayList<>();
}
