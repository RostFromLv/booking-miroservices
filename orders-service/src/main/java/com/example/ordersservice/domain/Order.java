package com.example.ordersservice.domain;

import com.example.bookingcommonabstractservice.converter.Convertible;
import com.example.commondto.common.Status;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


/**
 * Class that represent order in booking system.
 */
@Data
@With
@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Order implements Convertible {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column
  private Integer userId;

  @Column
  private Integer hotelId;

  @Column
  private Integer hotelRoomId;

  @Column
  private Long fromDate;

  @Column
  private Long endDate;

  @Column
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(nullable = false, updatable = false)
  @CreatedDate
  private Long createdAt;

  @Column
  private Long expiredAt;
}
