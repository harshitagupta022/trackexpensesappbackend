package pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.dto;

import pl.harshita_gupta.trackexpensesappbackend.financialtransaction.api.model.FinancialTransactionType;
import pl.harshita_gupta.trackexpensesappbackend.general.regex.RegexConstant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


public record FinancialTransactionCategoryCreateDTO(
        @NotBlank
        @Pattern(regexp = RegexConstant.CATEGORY_NAME_PATTERN)
        String name,
        @NotNull FinancialTransactionType type) {
}
