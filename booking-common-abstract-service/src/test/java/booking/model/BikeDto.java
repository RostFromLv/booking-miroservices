package booking.model;
import booking.domain.Type;
import com.example.bookingcommonabstractservice.converter.Convertible;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@With
@NoArgsConstructor
@AllArgsConstructor
public class BikeDto implements Convertible {
  @NotNull
  private Integer id;

  @NotNull
  private Type type;

  @NotNull
  private Float wheelRadius;
}
