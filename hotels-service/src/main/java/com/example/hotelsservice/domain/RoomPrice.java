package com.example.hotelsservice.domain;

import com.example.bookingcommonabstractservice.converter.Convertible;
import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "room_price")
public class RoomPrice  implements Convertible {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column
  private Double defaultPrice;
  @Column
  private Double price;
  @Column
  private Long startDate;
  @Column
  private Long endDate;

  @ToString.Exclude
  @OneToOne(mappedBy ="roomPrice" )
  @JsonBackReference
  private HotelRoom hotelRoom;
}
