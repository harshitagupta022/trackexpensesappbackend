package pl.harshita_gupta.trackexpensesappbackend.wallet.api.dto;

import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Builder
public record WalletCreateDTO(@NotBlank @Size(max = 20) @Pattern(regexp = "[\\w ]+") String name) {
}
