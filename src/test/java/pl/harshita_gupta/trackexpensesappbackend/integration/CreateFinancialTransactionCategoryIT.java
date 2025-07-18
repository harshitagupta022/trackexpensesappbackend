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
import pl.harshita_gupta.trackexpensesappbackend.TestUtils;
import pl.harshita_gupta.trackexpensesappbackend.auth.api.AuthRepository;
import pl.harshita_gupta.trackexpensesappbackend.auth.usermodel.User;
import pl.harshita_gupta.trackexpensesappbackend.financialtransaction.api.FinancialTransactionRepository;
import pl.harshita_gupta.trackexpensesappbackend.financialtransaction.api.model.FinancialTransactionType;
import pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.FinancialTransactionCategoryRepository;
import pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.dto.FinancialTransactionCategoryCreateDTO;
import pl.harshita_gupta.trackexpensesappbackend.general.exception.ErrorCode;
import pl.harshita_gupta.trackexpensesappbackend.wallet.api.WalletRepository;


class CreateFinancialTransactionCategoryIT extends BaseIntegrationTestIT {

    public static final long USER_ID_1L = 1L;

    @Autowired
    private FinancialTransactionRepository financialTransactionRepository;

    @Autowired
    private FinancialTransactionCategoryRepository financialTransactionCategoryRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private AuthRepository userRepository;


    @BeforeEach
    void clearDatabase() {
        financialTransactionRepository.deleteAll();
        financialTransactionCategoryRepository.deleteAll();
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }

    @DisplayName("Should return ResponseStatus IsCreated when create financial transaction category")
    @Test
    void testCreateFinancialTransactionCategory_whenValidDataProvided_thenShouldCreateCategory(
    ) throws Exception {
        User user = userRepository.save(TestUtils.createUserForTest());

        var financialTransactionCategoryCreateDTO
                = new FinancialTransactionCategoryCreateDTO("Category",
                FinancialTransactionType.INCOME);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryCreateDTO))
                        .with(SecurityMockMvcRequestPostProcessors.user(String.valueOf(user.getId())))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().isCreated(),
                        MockMvcResultMatchers.jsonPath("$.name").value("Category"),
                        MockMvcResultMatchers.jsonPath("$.type").value("INCOME")
                );

        Assertions.assertEquals(1, financialTransactionCategoryRepository.count());
    }

    @DisplayName("Should return ResponseStatus BadRequest when name length is greater than 30")
    @Test
    void testCreateFinancialTransactionCategory_whenNameExceeds30Characters_thenShouldReturnBadRequest()
            throws Exception {
        var financialTransactionCategoryCreateDTO
                = new FinancialTransactionCategoryCreateDTO("ThisIsVeryLongNameForCategoryMoreThan30Characters",
                FinancialTransactionType.INCOME);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryCreateDTO))
                        .with(SecurityMockMvcRequestPostProcessors.user(Long.toString(USER_ID_1L))))
                .andExpectAll(
                        MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.status")
                                .value(ErrorCode.TEA003.getBusinessStatus()),
                        MockMvcResultMatchers.jsonPath("$.message")
                                .value(ErrorCode.TEA003.getBusinessMessage()),
                        MockMvcResultMatchers.jsonPath("$.statusCode")
                                .value(ErrorCode.TEA003.getBusinessStatusCode())
                );

        Assertions.assertEquals(0, financialTransactionCategoryRepository.count());
    }

    @DisplayName("Should return ResponseStatus BadRequest when name is empty")
    @Test
    void testCreateFinancialTransactionCategory_whenNameIsEmpty_thenShouldReturnBadRequest() throws Exception {
        var financialTransactionCategoryCreateDTO = new FinancialTransactionCategoryCreateDTO("",
                FinancialTransactionType.INCOME);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryCreateDTO))
                        .with(SecurityMockMvcRequestPostProcessors.user(Long.toString(USER_ID_1L)))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpectAll(
                        MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.status")
                                .value(ErrorCode.TEA003.getBusinessStatus()),
                        MockMvcResultMatchers.jsonPath("$.message")
                                .value(ErrorCode.TEA003.getBusinessMessage()),
                        MockMvcResultMatchers.jsonPath("$.statusCode")
                                .value(ErrorCode.TEA003.getBusinessStatusCode())
                );
    }

    @DisplayName("Should return ResponseStatus BadRequest when name contains invalid characters")
    @Test
    void testCreateFinancialTransactionCategory_whenNameContainsInvalidCharacters_thenShouldReturnBadRequest(
    ) throws Exception {
        var categoryNameForWrongPathTest = "`-'+=|\\/?,.<>%&(){}[];:" + "\"";
        var financialTransactionCategoryCreateDTO = new FinancialTransactionCategoryCreateDTO(
                categoryNameForWrongPathTest,
                FinancialTransactionType.INCOME);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryCreateDTO))
                        .with(SecurityMockMvcRequestPostProcessors.user(Long.toString(USER_ID_1L))))
                .andExpectAll(
                        MockMvcResultMatchers.status().isBadRequest(),
                        MockMvcResultMatchers.jsonPath("$.status")
                                .value(ErrorCode.TEA003.getBusinessStatus()),
                        MockMvcResultMatchers.jsonPath("$.message")
                                .value(ErrorCode.TEA003.getBusinessMessage()),
                        MockMvcResultMatchers.jsonPath("$.statusCode")
                                .value(ErrorCode.TEA003.getBusinessStatusCode())
                );
    }

    @DisplayName("Should return ResponseStatus BadRequest when type is empty")
    @Test
    void testCreateFinancialTransactionCategory_whenTypeIsEmpty_thenShouldReturnBadRequest() throws Exception {
        var financialTransactionCategoryCreateDTO = new FinancialTransactionCategoryCreateDTO(
                "Category",
                null);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(financialTransactionCategoryCreateDTO))
                        .with(SecurityMockMvcRequestPostProcessors.user(Long.toString(USER_ID_1L))))
                .andExpectAll(
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
