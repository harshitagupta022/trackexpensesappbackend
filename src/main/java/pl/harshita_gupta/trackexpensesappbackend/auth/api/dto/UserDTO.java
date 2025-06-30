package pl.harshita_gupta.trackexpensesappbackend.auth.api.dto;

import lombok.Builder;
import pl.harshita_gupta.trackexpensesappbackend.auth.usermodel.UserStatus;

@Builder
public record UserDTO(Long id, String userName, String email, String password, UserStatus userStatus) {
}
