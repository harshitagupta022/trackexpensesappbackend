package pl.harshita_gupta.trackexpensesappbackend.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

class FindTransactionByIDIT extends BaseIntegrationTestIT {

    @Autowired
    private FinancialTransactionRepository financialTransactionRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private AuthRepository userRepository;

    @Autowired
    private AuthService authService;

    @BeforeEach
    public void clearDatabase() {
        financialTransactionRepository.deleteAll();
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("Should return proper financial transaction when search Id exist in database")
    @Test
    void testGetFinancialTransactionById_whenFindingTransactionWithExistingId_thenReturnFinancialTransactionWithCorrespondingId()
            throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());
        Wallet wallet = walletRepository.save(TestUtils.createWalletForTest(user));
        FinancialTransaction testFinancialTransaction = createTestFinancialTransaction(wallet, "description example");
        String accessToken = authService.createAccessToken(user);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/transactions/{id}", testFinancialTransaction.getId())
                .accept(MediaType.APPLICATION_JSON)
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken));

        resultActions.andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("$.id").value(testFinancialTransaction.getId()),
                MockMvcResultMatchers.jsonPath("$.amount").value(testFinancialTransaction.getAmount()),
                MockMvcResultMatchers.jsonPath("$.description").value(testFinancialTransaction.getDescription()),
                MockMvcResultMatchers.jsonPath("$.type").value(testFinancialTransaction.getType().toString()));

        Assertions.assertEquals(1, financialTransactionRepository.count());
    }

    @DisplayName("Should return status NOT_FOUND when search Id does not exist in database")
    @Test
    void testGetFinancialTransactionById_whenSearchIdDoesNotExistInDatabase_thenReturnErrorNotFound() throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());
        Wallet wallet = walletRepository.save(TestUtils.createWalletForTest(user));
        String accessToken = authService.createAccessToken(user);
        FinancialTransaction financialTransaction = createTestFinancialTransaction(wallet, "description example");

        final long notExistingFinancialTransactionId = 999L;
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(
                        "/api/transactions/{id}",
                        notExistingFinancialTransactionId)
                .accept(MediaType.APPLICATION_JSON)
                .header(BaseIntegrationTestIT.AUTHORIZATION, BaseIntegrationTestIT.BEARER + accessToken));

        resultActions.andExpectAll(MockMvcResultMatchers.status().isNotFound(),
                MockMvcResultMatchers.jsonPath("$.status").value(ErrorCode.FT001.getBusinessStatus()),
                MockMvcResultMatchers.jsonPath("$.message").value(ErrorCode.FT001.getBusinessMessage()),
                MockMvcResultMatchers.jsonPath("$.statusCode").value(ErrorCode.FT001.getBusinessStatusCode()));

        Assertions.assertEquals(1, financialTransactionRepository.count());
    }

    private FinancialTransaction createTestFinancialTransaction(Wallet wallet, String description) {
        return financialTransactionRepository.save(FinancialTransaction.builder()
                .wallet(wallet)
                .amount(new BigDecimal("5.0"))
                .date(Instant.ofEpochSecond(1L))
                .type(FinancialTransactionType.INCOME)
                .description(description)
                .build());
    }

}
