package pl.harshita_gupta.trackexpensesappbackend.auth.api.dto;

import javax.validation.constraints.NotBlank;

public record AuthAccessTokenDTO(@NotBlank String accessToken) {
}
