package booking.domain;

import com.example.bookingcommonabstractservice.converter.Convertible;
import javax.persistence.Column;
import javax.persistence.Entity;
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

@With
@Data
@Entity
@Table(name = "bike")
@AllArgsConstructor
@NoArgsConstructor
public class Bike implements Convertible {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column
  private Float wheelRadius;
  @Column
  @Enumerated(EnumType.STRING)
  private Type type;
}
