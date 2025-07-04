package pl.harshita_gupta.trackexpensesappbackend.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pl.harshita_gupta.trackexpensesappbackend.auth.api.AuthRepository;
import pl.harshita_gupta.trackexpensesappbackend.auth.usermodel.User;
import pl.harshita_gupta.trackexpensesappbackend.financialtransaction.api.model.FinancialTransactionType;
import pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.FinancialTransactionCategoryModelMapper;
import pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.FinancialTransactionCategoryRepository;
import pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.dto.FinancialTransactionCategoryDTO;
import pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.dto.FinancialTransactionCategoryUpdateDTO;
import pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.model.FinancialTransactionCategory;
import pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.impl.FinancialTransactionCategoryServiceImpl;
import pl.harshita_gupta.trackexpensesappbackend.general.exception.AppRuntimeException;
import pl.harshita_gupta.trackexpensesappbackend.general.exception.ErrorCode;

import java.time.Instant;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinancialTransactionCategoryUpdateServiceImplTest {

    private static final long VALID_ID = 1L;

    private static final long INVALID_ID = 10L;

    private static final long USER_ID_1L = 1L;

    private static final String NAME = "test_name";

    private static final FinancialTransactionType TYPE_INCOME = FinancialTransactionType.INCOME;

    private static final FinancialTransactionCategory VALID_CATEGORY
            = FinancialTransactionCategory.builder()
            .id(VALID_ID)
            .name(NAME)
            .type(TYPE_INCOME)
            .creationDate(Instant.now())
            .build();

    private static final FinancialTransactionCategoryDTO VALID_CATEGORY_DTO
            = new FinancialTransactionCategoryDTO(VALID_ID, NAME, TYPE_INCOME, USER_ID_1L);

    private static final FinancialTransactionCategoryUpdateDTO VALID_UPDATE_CATEGORY_DTO
            = new FinancialTransactionCategoryUpdateDTO(NAME, TYPE_INCOME);

    @Mock
    private FinancialTransactionCategoryRepository repository;

    @Mock
    private FinancialTransactionCategoryModelMapper mapper;

    @InjectMocks
    private FinancialTransactionCategoryServiceImpl service;

    @Mock
    private AuthRepository userRepository;

    @Test
    @DisplayName("update the FT category and return the category object if the ID is correct")
    void testUpdateFTCategoryById_WhenIdIsCorrect_ThenReturnCategoryEntity() {
        when(repository.findByIdAndUserId(VALID_ID, USER_ID_1L)).thenReturn(Optional.of(VALID_CATEGORY));
        when(mapper.mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(any()))
                .thenReturn(VALID_CATEGORY_DTO);
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        FinancialTransactionCategoryDTO dto
                = service.updateFinancialTransactionCategory(VALID_ID, USER_ID_1L, VALID_UPDATE_CATEGORY_DTO);

        assertEquals(VALID_CATEGORY_DTO, dto);
    }

    @Test
    @DisplayName("do not update FT Category if id is incorrect then throw AppRuntimeException and doesnt update entity")
    void testUpdateFTCategoryById_WhenIdIsIncorrect_ThenThrowError() {
        when(repository.findByIdAndUserId(INVALID_ID, USER_ID_1L)).thenReturn(empty());
        AppRuntimeException exception = assertThrows(
                AppRuntimeException.class,
                () -> service.updateFinancialTransactionCategory(INVALID_ID, USER_ID_1L, VALID_UPDATE_CATEGORY_DTO)
        );

        assertEquals(ErrorCode.FTC001.getBusinessStatusCode(), exception.getBusinessStatusCode());
        verify(repository, never()).save(any());
    }

}
