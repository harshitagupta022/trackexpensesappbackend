package pl.harshita_gupta.trackexpensesappbackend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.harshita_gupta.trackexpensesappbackend.auth.WebSecurityConfig;
import pl.harshita_gupta.trackexpensesappbackend.general.exception.ErrorCode;
import pl.harshita_gupta.trackexpensesappbackend.general.exception.ErrorStrategy;
import pl.harshita_gupta.trackexpensesappbackend.wallet.WalletController;
import pl.harshita_gupta.trackexpensesappbackend.wallet.api.WalletModelMapper;
import pl.harshita_gupta.trackexpensesappbackend.wallet.api.WalletService;
import pl.harshita_gupta.trackexpensesappbackend.wallet.api.dto.WalletDTO;
import pl.harshita_gupta.trackexpensesappbackend.wallet.api.dto.WalletUpdateDTO;
import pl.harshita_gupta.trackexpensesappbackend.wallet.impl.WalletServiceImpl;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.util.Objects;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WalletController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WalletServiceImpl.class),
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes =
                {WalletModelMapper.class, ErrorStrategy.class, WebSecurityConfig.class}))
@ActiveProfiles("test")
class WalletUpdateControllerTest {

    private static final Long WALLET_ID_0L = 0L;

    private static final Long USER_ID_0L = 0L;

    private static final Long WALLET_ID_1L = 1L;

    private static final Long USER_ID_1L = 1L;

    private static final String NAME_1 = "name one";

    public static final String INVALID_NAME = "@#$%^&";

    public static final String EMPTY_NAME = StringUtils.EMPTY;

    private static final Instant DATE_NOW = Instant.now();

    public static final String TOO_LONG_NAME_MORE_THAN_20_LETTERS = "Too long name - more than 20 letters.";

    @MockBean
    private WalletService walletService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("when update wallet data are correct should return response status OK")
    void shouldReturnResponseStatusOK_WhenUpdateWalletDataAreCorrect() throws Exception {
        // given
        WalletUpdateDTO updDTO = new WalletUpdateDTO(NAME_1);
        given(walletService.updateWallet(WALLET_ID_1L, updDTO, USER_ID_1L))
                .willReturn(new WalletDTO(WALLET_ID_1L, updDTO.name(), DATE_NOW, USER_ID_1L));

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/wallets/{id}", WALLET_ID_1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(WALLET_ID_1L.intValue())))
                .andExpect(jsonPath("$.name", Matchers.equalTo(updDTO.name())));
    }

    @Test
    @DisplayName("when wallet name is empty should return response status bad request")
    void shouldReturnResponseStatusBadRequest_WhenWalletNameIsEmpty() throws Exception {
        // given
        WalletUpdateDTO updDTO = new WalletUpdateDTO(EMPTY_NAME);
        given(walletService.updateWallet(WALLET_ID_1L, updDTO, USER_ID_1L))
                .willReturn(new WalletDTO(WALLET_ID_1L, EMPTY_NAME, DATE_NOW, USER_ID_1L));

        //when
        ResultActions resultActions = mockMvc.perform(patch("/api/wallets/{id}", WALLET_ID_1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("when wallet name is changed return response status OK and correct response body")
    void shouldReturnStatusOKAndCorrectResponseBody_WhenWalletNameIsChanged() throws Exception {
        // given
        WalletUpdateDTO updDTO = new WalletUpdateDTO(NAME_1);
        given(walletService.updateWallet(WALLET_ID_1L, updDTO, USER_ID_1L))
                .willReturn(new WalletDTO(WALLET_ID_1L, updDTO.name(), DATE_NOW, USER_ID_1L));

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/wallets/{id}", WALLET_ID_1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.equalTo(WALLET_ID_1L.intValue())))
                .andExpect(jsonPath("$.name", Matchers.equalTo(updDTO.name())));
    }

    @Test
    @DisplayName("when wallet name is too long should return response status bad request")
    void shouldReturnResponseStatusBadRequest_WhenWalletNameIsTooLong() throws Exception {
        // given
        WalletUpdateDTO updDTO = new WalletUpdateDTO(TOO_LONG_NAME_MORE_THAN_20_LETTERS);
        given(walletService.updateWallet(WALLET_ID_1L, updDTO, USER_ID_1L))
                .willReturn(new WalletDTO(WALLET_ID_1L, EMPTY_NAME, DATE_NOW, USER_ID_1L));

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/wallets/{id}", WALLET_ID_1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("when wallet name contains illegal letters should return response status bad request")
    void shouldReturnResponseStatusBadRequest_WhenWalletNameContainsIllegalLetters() throws Exception {
        // given
        WalletUpdateDTO updDTO = new WalletUpdateDTO(INVALID_NAME);
        given(walletService.updateWallet(WALLET_ID_1L, updDTO, USER_ID_1L))
                .willReturn(new WalletDTO(WALLET_ID_1L, EMPTY_NAME, DATE_NOW, USER_ID_1L));

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/wallets/{id}", WALLET_ID_1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("when wallet id is null should return response status server error")
    void shouldReturnResponseStatusServerError_WhenWalletIdIsNull() throws Exception {
        // given
        WalletUpdateDTO updDTO = new WalletUpdateDTO(INVALID_NAME);
        given(walletService.updateWallet(null, updDTO, USER_ID_1L))
                .willReturn(new WalletDTO(null, EMPTY_NAME, DATE_NOW, USER_ID_1L));

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/wallets/" + null)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpectAll(
                MockMvcResultMatchers.status().is5xxServerError(),
                MockMvcResultMatchers.jsonPath("$.status")
                        .value(ErrorCode.TEA004.getBusinessStatus()),
                MockMvcResultMatchers.jsonPath("$.message")
                        .value(ErrorCode.TEA004.getBusinessMessage()),
                MockMvcResultMatchers.jsonPath("$.statusCode")
                        .value(ErrorCode.TEA004.getBusinessStatusCode())
        );
    }

    @Test
    @DisplayName("when wallet id is zero should return response status bad request")
    void shouldReturnResponseStatusBadRequest_WhenWalletIdIsZero() throws Exception {
        // given
        WalletUpdateDTO updDTO = new WalletUpdateDTO(NAME_1);
//        given(walletService.updateWallet(WALLET_ID_0L, updDTO, USER_ID_1L))
//                .willReturn(new WalletDTO(WALLET_ID_0L, EMPTY_NAME, DATE_NOW, USER_ID_1L));

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/wallets/{id}", WALLET_ID_0L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpectAll(
                MockMvcResultMatchers.status().isBadRequest(),
                MockMvcResultMatchers.jsonPath("$.status")
                        .value(ErrorCode.TEA003.getBusinessStatus()),
                MockMvcResultMatchers.jsonPath("$.message")
                        .value(ErrorCode.TEA003.getBusinessMessage()),
                MockMvcResultMatchers.jsonPath("$.statusCode")
                        .value(ErrorCode.TEA003.getBusinessStatusCode())
        );
    }

    @Test
    @DisplayName("when wallet id is negative should return response status bad request")
    void shouldReturnResponseStatusBadRequestWhenWalletIdIsNegative() throws Exception {
        // given
        WalletUpdateDTO updDTO = new WalletUpdateDTO(NAME_1);
        given(walletService.updateWallet(-WALLET_ID_1L, updDTO, USER_ID_1L))
                .willReturn(new WalletDTO(-WALLET_ID_1L, EMPTY_NAME, DATE_NOW, USER_ID_1L));

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/wallets/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpectAll(
                MockMvcResultMatchers.status().isBadRequest(),
                MockMvcResultMatchers.jsonPath("$.status")
                        .value(ErrorCode.TEA003.getBusinessStatus()),
                MockMvcResultMatchers.jsonPath("$.message")
                        .value(ErrorCode.TEA003.getBusinessMessage()),
                MockMvcResultMatchers.jsonPath("$.statusCode")
                        .value(ErrorCode.TEA003.getBusinessStatusCode())
        );
    }

    @Test
    @DisplayName("when wallet name is empty should return response status bad request")
    void shouldReturnResponseStatusBadRequestWhenWalletNameIsEmpty() throws Exception {
        // given
        WalletUpdateDTO updDTO = new WalletUpdateDTO(EMPTY_NAME);
        given(walletService.updateWallet(WALLET_ID_1L, updDTO, USER_ID_1L))
                .willReturn(new WalletDTO(1L, EMPTY_NAME, DATE_NOW, USER_ID_1L));

        //when
        ResultActions resultActions = mockMvc.perform(patch("/api/wallets/{id}", WALLET_ID_1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpectAll(
                MockMvcResultMatchers.status().isBadRequest(),
                MockMvcResultMatchers.jsonPath("$.status")
                        .value(ErrorCode.TEA003.getBusinessStatus()),
                MockMvcResultMatchers.jsonPath("$.message")
                        .value(ErrorCode.TEA003.getBusinessMessage()),
                MockMvcResultMatchers.jsonPath("$.statusCode")
                        .value(ErrorCode.TEA003.getBusinessStatusCode())
        );
    }

    @Test
    @DisplayName("when wallet name is too long should return response status bad request")
    void shouldReturnResponseStatusBadRequestWhenWalletNameIsTooLong() throws Exception {
        // given
        WalletUpdateDTO updDTO = new WalletUpdateDTO(TOO_LONG_NAME_MORE_THAN_20_LETTERS);
        given(walletService.updateWallet(WALLET_ID_1L, updDTO, USER_ID_1L))
                .willReturn(new WalletDTO(1L, EMPTY_NAME, DATE_NOW, USER_ID_1L));

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/wallets/{id}", WALLET_ID_1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpectAll(
                MockMvcResultMatchers.status().isBadRequest(),
                MockMvcResultMatchers.jsonPath("$.status")
                        .value(ErrorCode.TEA003.getBusinessStatus()),
                MockMvcResultMatchers.jsonPath("$.message")
                        .value(ErrorCode.TEA003.getBusinessMessage()),
                MockMvcResultMatchers.jsonPath("$.statusCode")
                        .value(ErrorCode.TEA003.getBusinessStatusCode())
        );
    }

    @Test
    @DisplayName("when wallet name contains illegal letters should return response status bad request")
    void shouldReturnResponseStatusBadRequestWhenWalletNameContainsIllegalLetters() throws Exception {
        // given
        WalletUpdateDTO updDTO = new WalletUpdateDTO(INVALID_NAME);
        given(walletService.updateWallet(WALLET_ID_1L, updDTO, USER_ID_1L))
                .willThrow(new RuntimeException("Wallet name contains illegal letters"));

        // when
        ResultActions resultActions = mockMvc.perform(patch("/api/wallets/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(Objects.requireNonNull(objectMapper.writeValueAsString(updDTO)))
                .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(USER_ID_1L))));

        // then
        resultActions.andExpectAll(
                MockMvcResultMatchers.status().isBadRequest(),
                MockMvcResultMatchers.jsonPath("$.status")
                        .value(ErrorCode.TEA003.getBusinessStatus()),
                MockMvcResultMatchers.jsonPath("$.message")
                        .value(ErrorCode.TEA003.getBusinessMessage()),
                MockMvcResultMatchers.jsonPath("$.statusCode")
                        .value(ErrorCode.TEA003.getBusinessStatusCode())
        );
    }

}
