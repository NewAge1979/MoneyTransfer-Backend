package pl.chrapatij.moneytransfer.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Amount {
    @Min(0)
    private int value;

    @NotBlank
    private String currency;
}