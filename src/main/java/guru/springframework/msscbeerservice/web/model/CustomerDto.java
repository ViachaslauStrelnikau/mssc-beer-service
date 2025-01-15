package guru.springframework.msscbeerservice.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1605711145105979286L;

    @Null
    private UUID id;
    @NotBlank
    @Length(min = 3, max = 100)
    private String name;
}
