package pl.harshita_gupta.trackexpensesappbackend.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import pl.harshita_gupta.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.harshita_gupta.trackexpensesappbackend.auth.api.AuthRepository;
import pl.harshita_gupta.trackexpensesappbackend.auth.api.dto.UserDTO;
import pl.harshita_gupta.trackexpensesappbackend.auth.usermodel.User;
import pl.harshita_gupta.trackexpensesappbackend.auth.usermodel.UserStatus;
import pl.harshita_gupta.trackexpensesappbackend.wallet.api.WalletRepository;
import pl.harshita_gupta.trackexpensesappbackend.wallet.api.dto.WalletCreateDTO;

class WalletTestIT extends BaseIntegrationTestIT {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private AuthRepository userRepository;

    @BeforeEach
    public void cleanTable() {
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("It should create wallet")
    void shouldCreateWallet() throws Exception {
        User user = createTestUser();
        WalletCreateDTO walletCreateDTO =
                new WalletCreateDTO("Test name");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/wallets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(walletCreateDTO))
                        .accept(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(user.getId())))
                )
                .andExpect(MockMvcResultMatchers.status().isCreated());

        Assertions.assertEquals(1, walletRepository.findAll().size());
    }

    private User createTestUser() {
        final User userOne = User.builder()
                .userName("userone")
                .email("Email@wp.pl")
                .password("password1@")
                .userStatus(UserStatus.VERIFIED)
                .build();
        return userRepository.save(userOne);
    }

    private UserDTO createTestUserDTO() {
        return UserDTO.builder()
                .id(1L)
                .userName("userone")
                .email("Email@wp.pl")
                .password("password1@")
                .userStatus(UserStatus.VERIFIED)
                .build();
    }

}
