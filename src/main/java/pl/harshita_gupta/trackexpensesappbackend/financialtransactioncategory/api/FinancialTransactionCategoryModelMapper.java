package pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.model.FinancialTransactionCategory;
import pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.dto.FinancialTransactionCategoryDTO;

@Mapper(componentModel = "spring")
public interface FinancialTransactionCategoryModelMapper {

    @Mapping(source = "user.id", target = "userId")
    FinancialTransactionCategoryDTO mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(
            FinancialTransactionCategory category);

}
