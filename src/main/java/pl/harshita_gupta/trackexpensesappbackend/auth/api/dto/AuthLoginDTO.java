package pl.harshita_gupta.trackexpensesappbackend.auth.api.dto;

import pl.harshita_gupta.trackexpensesappbackend.general.regex.RegexConstant;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public record AuthLoginDTO(@NotBlank @Pattern(regexp = RegexConstant.EMAIL_PATTERN) String email,
                           @NotBlank @Pattern(regexp = RegexConstant.PASSWORD_PATTERN)
                           String password, @NotNull Boolean isRememberMe) { }
