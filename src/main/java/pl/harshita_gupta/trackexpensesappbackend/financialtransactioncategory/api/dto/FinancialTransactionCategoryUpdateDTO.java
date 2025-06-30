package pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.dto;

import pl.harshita_gupta.trackexpensesappbackend.financialtransaction.api.model.FinancialTransactionType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public record FinancialTransactionCategoryUpdateDTO(@NotBlank String name, @NotNull FinancialTransactionType type) {
}
