package pl.harshita_gupta.trackexpensesappbackend.financialtransaction.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.harshita_gupta.trackexpensesappbackend.financialtransaction.api.dto.FinancialTransactionDTO;
import pl.harshita_gupta.trackexpensesappbackend.financialtransaction.api.model.FinancialTransaction;

@Mapper(componentModel = "spring")
public interface FinancialTransactionModelMapper {
    @Mapping(source = "financialTransactionCategory.id", target = "categoryId")
    FinancialTransactionDTO mapFinancialTransactionEntityToFinancialTransactionDTO(FinancialTransaction transaction);
}
