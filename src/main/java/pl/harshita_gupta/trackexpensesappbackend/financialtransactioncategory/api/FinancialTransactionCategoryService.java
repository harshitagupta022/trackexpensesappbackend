package pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.dto.FinancialTransactionCategoryCreateDTO;
import pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.dto.FinancialTransactionCategoryDTO;
import pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.dto.FinancialTransactionCategoryDetailedDTO;
import pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.dto.FinancialTransactionCategoryUpdateDTO;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface FinancialTransactionCategoryService {

    FinancialTransactionCategoryDTO updateFinancialTransactionCategory(
            @Min(1) @NotNull Long id, Long userId, @Valid FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO);

    FinancialTransactionCategoryDTO createFinancialTransactionCategory(
            @Valid @RequestBody FinancialTransactionCategoryCreateDTO financialTransactionCategoryCreateDTO, Long userId);

    FinancialTransactionCategoryDetailedDTO findCategoryForUser(@Min(1) @NotNull Long categoryId, Long userId);

    List<FinancialTransactionCategoryDTO> getFinancialTransactionCategories(Long userId);

    void deleteFinancialTransactionCategory(@Min(1) @NotNull Long categoryId, Long userId);

}

