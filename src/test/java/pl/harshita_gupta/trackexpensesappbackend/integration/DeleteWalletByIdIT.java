package pl.harshita_gupta.trackexpensesappbackend.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import pl.harshita_gupta.trackexpensesappbackend.BaseIntegrationTestIT;
import pl.harshita_gupta.trackexpensesappbackend.TestUtils;
import pl.harshita_gupta.trackexpensesappbackend.auth.api.AuthRepository;
import pl.harshita_gupta.trackexpensesappbackend.auth.api.AuthService;
import pl.harshita_gupta.trackexpensesappbackend.auth.usermodel.User;
import pl.harshita_gupta.trackexpensesappbackend.financialtransaction.api.FinancialTransactionRepository;
import pl.harshita_gupta.trackexpensesappbackend.financialtransaction.api.model.FinancialTransaction;
import pl.harshita_gupta.trackexpensesappbackend.financialtransaction.api.model.FinancialTransactionType;
import pl.harshita_gupta.trackexpensesappbackend.general.exception.ErrorCode;
import pl.harshita_gupta.trackexpensesappbackend.wallet.api.WalletRepository;
import pl.harshita_gupta.trackexpensesappbackend.wallet.api.model.Wallet;

import java.math.BigDecimal;
import java.time.Instant;


class DeleteWalletByIdIT extends BaseIntegrationTestIT {

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private FinancialTransactionRepository financialTransactionRepository;

    @Autowired
    private AuthRepository userRepository;

    @Autowired
    private AuthService authService;

    @BeforeEach
    void clearDatabase() {

        walletRepository.deleteAll();
        financialTransactionRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("Should delete wallet from a database and return status 'OK'")
    @Test
    void testDeleteWalletByIdAPI_whenWalletIdIsCorrect_thenShouldReturnAcceptAndDeleteRecord() throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());

        Wallet wallet = walletRepository.save(TestUtils.createWalletForTest(user));
        FinancialTransaction testFinancialTransaction = createTestFinancialTransaction(wallet);
        String accessToken = authService.createAccessToken(user);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/wallets/{id}", wallet.getId())
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken));

        resultActions.andExpect(MockMvcResultMatchers.status().isOk());
        Assertions.assertEquals(0, walletRepository.count());
        Assertions.assertEquals(0, financialTransactionRepository.count());
    }

    @DisplayName("Should return is Not Found error when Id does not exist in a database")
    @Test
    void testDeleteWalletById_whenWalletIdIsIncorrect_thenShouldReturnNotFoundError() throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());
        Wallet wallet = walletRepository.save(TestUtils.createWalletForTest(user));
        String accessToken = authService.createAccessToken(user);

        final long notExistingWalletId = 999L;
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/api/wallets/{id}", notExistingWalletId)
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken));

        resultActions.andExpectAll(MockMvcResultMatchers.status().isNotFound(),
                MockMvcResultMatchers.jsonPath("$.status").value(ErrorCode.W003.getBusinessStatus()),
                MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.W003.getBusinessMessage()),
                MockMvcResultMatchers.jsonPath("$.statusCode").value(ErrorCode.W003.getBusinessStatusCode()));

        Assertions.assertEquals(1, walletRepository.count());
        Assertions.assertEquals(0, financialTransactionRepository.count());
    }

    private FinancialTransaction createTestFinancialTransaction(Wallet wallet) {
        return financialTransactionRepository.save(FinancialTransaction.builder()
                .wallet(wallet)
                .amount(new BigDecimal("2.0"))
                .date(Instant.ofEpochSecond(1L))
                .type(FinancialTransactionType.INCOME)
                .description("Test transaction")
                .build());
    }

}
