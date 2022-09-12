package com.example.hotelsservice.domain;

import com.example.bookingcommonabstractservice.converter.Convertible;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservation")
public class Reservation implements Convertible {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column
  private Integer hotelId;
  @Column
  private Integer roomId;
  @Column
  private Long dateFrom;
  @Column
  private Long dateTo;
}
