package com.example.usersservice.domain;

import com.example.bookingcommonabstractservice.converter.Convertible;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The class that represent user as a person.
 *
 * @author Rostyslav
 * @since 1.0.0-SNAPSHOT
 */
@Data
@With
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User implements Convertible {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column
  private String firstName;
  @Column
  private String lastName;
  @Column(unique = true)
  private String email;
  @Column
  private String phoneNumber;
  @Column(nullable = false, updatable = false)
  @CreatedDate
  private long createdAt;
  @Column
  @LastModifiedDate
  private long updatedAt;
}
