package pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.dto;

import java.math.BigInteger;

public record FinancialTransactionCategoryDetailedDTO(FinancialTransactionCategoryDTO financialTransactionCategoryDTO,
                                                      BigInteger financialTransactionsCounter) {

}
