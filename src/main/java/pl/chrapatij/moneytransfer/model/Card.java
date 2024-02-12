package pl.chrapatij.moneytransfer.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class Card {
    @NotBlank
    @Size(min = 16, max = 16)
    @Pattern(regexp = "(?<!\\d)\\d{16}(?!\\d)")
    private String number;

    @NotBlank
    @Size(min = 5, max = 5)
    @Pattern(regexp = "(0[1-9]|1[0-2])/\\d{2}")
    private String validTill;

    @NotBlank
    @Size(min = 3, max = 3)
    @Pattern(regexp = "(?<!\\d)\\d{3}(?!\\d)")
    private String cvv;

    private boolean isCreditCard;

    private int balance;

    @Min(0)
    private int limit;

    @NotBlank
    private String currency;
}