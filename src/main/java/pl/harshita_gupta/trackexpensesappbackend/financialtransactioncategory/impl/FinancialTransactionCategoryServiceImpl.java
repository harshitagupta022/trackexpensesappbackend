package pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.model.FinancialTransactionCategory;
import pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.FinancialTransactionCategoryModelMapper;
import pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.FinancialTransactionCategoryRepository;
import pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.FinancialTransactionCategoryService;
import pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.dto.FinancialTransactionCategoryCreateDTO;
import pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.dto.FinancialTransactionCategoryDTO;
import pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.dto.FinancialTransactionCategoryDetailedDTO;
import pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.dto.FinancialTransactionCategoryUpdateDTO;
import pl.harshita_gupta.trackexpensesappbackend.general.exception.AppRuntimeException;
import pl.harshita_gupta.trackexpensesappbackend.general.exception.ErrorCode;
import pl.harshita_gupta.trackexpensesappbackend.auth.usermodel.User;
import pl.harshita_gupta.trackexpensesappbackend.financialtransaction.api.FinancialTransactionRepository;
import pl.harshita_gupta.trackexpensesappbackend.auth.api.AuthRepository;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;
import java.util.List;

@RequiredArgsConstructor
@Service
@Validated
public class FinancialTransactionCategoryServiceImpl implements FinancialTransactionCategoryService {

    private final FinancialTransactionCategoryRepository financialTransactionCategoryRepository;

    private final FinancialTransactionCategoryModelMapper financialTransactionCategoryModelMapper;

    private final FinancialTransactionRepository financialTransactionRepository;

    private final AuthRepository userRepository;

    @Override
    public FinancialTransactionCategoryDTO createFinancialTransactionCategory(
            @Valid FinancialTransactionCategoryCreateDTO financialTransactionCategoryCreateDTO,
            Long userId) {

        User user = getUserByUserId(userId);

        FinancialTransactionCategory financialTransactionCategory = FinancialTransactionCategory
                .builder().name(financialTransactionCategoryCreateDTO.name()).type(financialTransactionCategoryCreateDTO
                        .type()).user(user).build();

        FinancialTransactionCategory savedEntity = financialTransactionCategoryRepository
                .save(financialTransactionCategory);

        return financialTransactionCategoryModelMapper
                .mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(savedEntity);
    }

    @Override
    public FinancialTransactionCategoryDetailedDTO findCategoryForUser(@Min(1) @NotNull Long categoryId, Long userId) {

        FinancialTransactionCategory financialTransactionCategory = financialTransactionCategoryRepository
                .findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new AppRuntimeException(ErrorCode.FTC001,
                        String.format("Financial transaction category with id: %d not found for user ", categoryId)));

        BigInteger numberOfFinancialTransactions =
                financialTransactionRepository.countFinancialTransactionsByFinancialTransactionCategoryId(categoryId);

        FinancialTransactionCategoryDTO financialTransactionCategoryDTO = financialTransactionCategoryModelMapper
                .mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(financialTransactionCategory);

        return new FinancialTransactionCategoryDetailedDTO(
                financialTransactionCategoryDTO, numberOfFinancialTransactions);
    }


    @Override
    public List<FinancialTransactionCategoryDTO> getFinancialTransactionCategories(Long userId) {

        List<FinancialTransactionCategory> financialTransactionCategories = financialTransactionCategoryRepository
                .findAllByUserId(userId)
                .orElseThrow(() -> new AppRuntimeException(ErrorCode.FTC001,
                        String.format("Financial transaction categories not found for user with id: %d", userId)));

        return financialTransactionCategories.stream()
                .map(financialTransactionCategoryModelMapper
                        ::mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO)
                .toList();
    }


    @Override
    public void deleteFinancialTransactionCategory(@Min(1) @NotNull Long categoryId, Long userId) {
        if (financialTransactionCategoryRepository.existsByIdAndUserId(categoryId, userId)) {
            financialTransactionCategoryRepository.deleteById(categoryId);
        } else {
            throw new AppRuntimeException(
                    ErrorCode.FTC001,
                    String.format("Financial transaction category with id: %d not found for user ", categoryId));
        }
    }

    @Transactional
    public FinancialTransactionCategoryDTO updateFinancialTransactionCategory(
            Long categoryId, Long userId, FinancialTransactionCategoryUpdateDTO financialTransactionCategoryUpdateDTO) {
        FinancialTransactionCategory financialTransactionCategory
                = financialTransactionCategoryRepository.findByIdAndUserId(categoryId, userId)
                .orElseThrow(() -> new AppRuntimeException(ErrorCode.FTC001,
                        String.format("Financial transaction category with id: %d not found for user ", categoryId)));

        User user = getUserByUserId(userId);

        financialTransactionCategory.setName(financialTransactionCategoryUpdateDTO.name());
        financialTransactionCategory.setType(financialTransactionCategoryUpdateDTO.type());
        financialTransactionCategory.setUser(user);

        return financialTransactionCategoryModelMapper
                .mapFinancialTransactionCategoryEntityToFinancialTransactionCategoryDTO(financialTransactionCategory);
    }

    private User getUserByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new AppRuntimeException(ErrorCode.U005, String.format("User with id: %d doesn't exist.", userId)));
    }

}
