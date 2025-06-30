package pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.dto;

import pl.harshita_gupta.trackexpensesappbackend.financialtransaction.api.model.FinancialTransactionType;

public record FinancialTransactionCategoryDTO(Long id, String name, FinancialTransactionType type, Long userId) { }
