package pl.harshita_gupta.trackexpensesappbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.harshita_gupta.trackexpensesappbackend.auth.WebSecurityConfig;
import pl.harshita_gupta.trackexpensesappbackend.auth.api.dto.UserDTO;
import pl.harshita_gupta.trackexpensesappbackend.auth.usermodel.UserStatus;
import pl.harshita_gupta.trackexpensesappbackend.general.exception.ErrorStrategy;
import pl.harshita_gupta.trackexpensesappbackend.wallet.WalletController;
import pl.harshita_gupta.trackexpensesappbackend.wallet.api.WalletModelMapper;
import pl.harshita_gupta.trackexpensesappbackend.wallet.api.WalletService;
import pl.harshita_gupta.trackexpensesappbackend.wallet.api.dto.WalletCreateDTO;
import pl.harshita_gupta.trackexpensesappbackend.wallet.api.dto.WalletDTO;
import pl.harshita_gupta.trackexpensesappbackend.wallet.impl.WalletServiceImpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;
import java.util.Objects;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WalletController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WalletServiceImpl.class),
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes =
                {WalletModelMapper.class, ErrorStrategy.class, WebSecurityConfig.class}))
@ActiveProfiles("test")
class WalletCreateControllerTest {

    private static final Long WALLET_ID_1L = 1L;

    private static final Long USER_ID_1L = 1L;

    private static final String INVALID_NAME = "@#$%^&";

    private static final Instant DATE_NOW = Instant.now();

    public static final String TOO_LONG_NAME_MORE_THAN_20_LETTERS = "Too long name - more than 20 letters.";

    @MockBean
    private WalletService walletService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("when wallet name contains illegal letters should return response status bad request")
    void shouldNotCreateWalletAndReturnResponseStatusBadRequestStatus_WhenWalletNameContainsIllegalLetters() throws Exception {
        // given
        WalletCreateDTO dto = new WalletCreateDTO(INVALID_NAME);
        given(walletService.createWallet(dto, USER_ID_1L))
                .willReturn(new WalletDTO(WALLET_ID_1L, INVALID_NAME, DATE_NOW, USER_ID_1L));

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(dto)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("when wallet name is empty should return response status bad request")
    void shouldReturnResponseStatusBadRequest_WhenWalletNameIsEmpty() throws Exception {
        // given
        WalletCreateDTO dto = new WalletCreateDTO("");
        given(walletService.createWallet(dto, USER_ID_1L))
                .willReturn(new WalletDTO(WALLET_ID_1L, "", DATE_NOW, USER_ID_1L));

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(dto)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("when wallet name is null should return response status bad request")
    void shouldReturnResponseStatusBadRequest_WhenWalletNameIsNull() throws Exception {
        // given
        WalletCreateDTO dto = new WalletCreateDTO(null);
        given(walletService.createWallet(dto, USER_ID_1L))
                .willReturn(new WalletDTO(WALLET_ID_1L, null, DATE_NOW, USER_ID_1L));

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(dto)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("when wallet name is too long should return response status bad request")
    void shouldReturnResponseStatusBadRequest_WhenWalletNameIsTooLong() throws Exception {
        // given
        WalletCreateDTO dto = new WalletCreateDTO(TOO_LONG_NAME_MORE_THAN_20_LETTERS);
        given(walletService.createWallet(dto, USER_ID_1L))
                .willReturn(new WalletDTO(WALLET_ID_1L, TOO_LONG_NAME_MORE_THAN_20_LETTERS, DATE_NOW, USER_ID_1L));

        // when
        ResultActions resultActions = mockMvc.perform(post("/api/wallets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(dto)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpect(status().isBadRequest());
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
