package pl.chrapatij.moneytransfer.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ConfirmRequestBody {
    @NotBlank
    private String operationId;

    @Size(min = 4, max = 6)
    @Pattern(regexp = "(?<!\\d)\\d{4,6}(?!\\d)")
    private String code;
}