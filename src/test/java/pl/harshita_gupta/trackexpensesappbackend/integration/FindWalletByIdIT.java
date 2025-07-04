package pl.harshita_gupta.trackexpensesappbackend.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import pl.harshita_gupta.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.harshita_gupta.trackexpensesappbackend.TestUtils;
import pl.harshita_gupta.trackexpensesappbackend.auth.api.AuthRepository;
import pl.harshita_gupta.trackexpensesappbackend.auth.api.AuthService;
import pl.harshita_gupta.trackexpensesappbackend.auth.api.dto.AuthLoginDTO;
import pl.harshita_gupta.trackexpensesappbackend.auth.usermodel.User;
import pl.harshita_gupta.trackexpensesappbackend.wallet.api.WalletRepository;
import pl.harshita_gupta.trackexpensesappbackend.wallet.api.dto.WalletCreateDTO;
import pl.harshita_gupta.trackexpensesappbackend.wallet.api.model.Wallet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class FindWalletByIdIT extends BaseIntegrationTestIT {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private AuthRepository userRepository;
    @Autowired
    private AuthService authService;

    @BeforeEach
    void clearDatabase() {
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("It should return wallet DTO by given id")
    @Test
    void testFindWalletByIdAPI_whenWalletIdIsCorrect_thenReturnWalletDTO() throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());
        String accessToken = authService.createAccessToken(user);
        Wallet wallet = walletRepository.save(TestUtils.createWalletForTest(user));

        ResultActions resultActions = mockMvc.perform(get("/api/wallets/{id}", wallet.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken));

        resultActions.andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("$.id").value(wallet.getId()),
                MockMvcResultMatchers.jsonPath("$.name").value(wallet.getName()));
        Assertions.assertEquals(1, walletRepository.count());
    }

    @DisplayName("It should return status Not Found when it cannot find by given id")
    @Test
    void testFindWalletByIdAPI_whenWalletIdIsIncorrect_thenReturnErrorResponse() throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());
        String accessToken = authService.createAccessToken(user);

        WalletCreateDTO testWalletDto = new WalletCreateDTO("TestWalletName");

        mockMvc.perform(get("/api/wallets/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testWalletDto))
                        .accept(MediaType.APPLICATION_JSON)
                        .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Assertions.assertEquals(0, walletRepository.count());
    }

    private AuthLoginDTO createAuthLoginDtoTest() {
        return new AuthLoginDTO("email@wp.pl", "Password1@", true);
    }

}
