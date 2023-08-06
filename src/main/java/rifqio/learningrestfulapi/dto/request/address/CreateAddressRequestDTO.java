package rifqio.learningrestfulapi.dto.request.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class CreateAddressRequestDTO {
    @NotNull
    @JsonIgnore
    private String contactId;

    @Size(max = 200)
    private String street;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String province;

    @NotNull
    @Size(max = 100)
    private String country;

    @Size(max = 10)
    private String postalCode;
}
