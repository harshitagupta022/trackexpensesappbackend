package pl.harshita_gupta.trackexpensesappbackend.financialtransaction.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.harshita_gupta.trackexpensesappbackend.financialtransaction.api.FinancialTransactionModelMapper;
import pl.harshita_gupta.trackexpensesappbackend.financialtransaction.api.FinancialTransactionRepository;
import pl.harshita_gupta.trackexpensesappbackend.financialtransaction.api.FinancialTransactionService;
import pl.harshita_gupta.trackexpensesappbackend.financialtransaction.api.dto.FinancialTransactionCreateDTO;
import pl.harshita_gupta.trackexpensesappbackend.financialtransaction.api.dto.FinancialTransactionDTO;
import pl.harshita_gupta.trackexpensesappbackend.financialtransaction.api.dto.FinancialTransactionUpdateDTO;
import pl.harshita_gupta.trackexpensesappbackend.financialtransaction.api.model.FinancialTransaction;
import pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.FinancialTransactionCategoryRepository;
import pl.harshita_gupta.trackexpensesappbackend.financialtransactioncategory.api.model.FinancialTransactionCategory;
import pl.harshita_gupta.trackexpensesappbackend.general.exception.AppRuntimeException;
import pl.harshita_gupta.trackexpensesappbackend.general.exception.ErrorCode;
import pl.harshita_gupta.trackexpensesappbackend.wallet.api.WalletRepository;
import pl.harshita_gupta.trackexpensesappbackend.wallet.api.model.Wallet;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class FinancialTransactionServiceImpl implements FinancialTransactionService {

    private final FinancialTransactionRepository financialTransactionRepository;

    private final FinancialTransactionModelMapper financialTransactionModelMapper;

    private final WalletRepository walletRepository;

    private final FinancialTransactionCategoryRepository financialTransactionCategoryRepository;


    @Override
    public FinancialTransactionDTO createFinancialTransaction(
            @Valid FinancialTransactionCreateDTO ftCreateDTO, Long userId) {
        Long walletId = ftCreateDTO.walletId();
        Wallet wallet = walletRepository.findByIdAndUserId(walletId, userId).orElseThrow(() -> {
            throw new AppRuntimeException(ErrorCode.W003, String.format("Wallet with id: %d does not exist", walletId));
        });
        FinancialTransactionCategory ftCategory =
                findFinancialTransactionCategory(ftCreateDTO.categoryId(), userId);

        if (ftCreateDTO.categoryId() != null && ftCreateDTO.type() != ftCategory.getType()) {
            throw new AppRuntimeException(ErrorCode.FT002,
                    String.format("Financial transaction type: '%s' and financial transaction category type"
                                    + " '%s' does not match",
                            ftCreateDTO.type().name(), ftCategory.getType()));
        }

        FinancialTransaction financialTransaction =
                buildFinancialTransaction(ftCreateDTO, wallet, ftCategory);
        FinancialTransaction savedFinancialTransaction = financialTransactionRepository.save(financialTransaction);

        return financialTransactionModelMapper.mapFinancialTransactionEntityToFinancialTransactionDTO(
                savedFinancialTransaction);
    }

    @Override
    public List<FinancialTransactionDTO> getFinancialTransactionsByWalletId(
            @Min(1) @NotNull Long walletId, Long userId) {
        if (!walletRepository.existsById(walletId)) {
            throw new AppRuntimeException(ErrorCode.W003, String.format("Wallet with id: %d does not exist", walletId));
        }

        return financialTransactionRepository.findAllByWalletIdAndWalletUserIdOrderByDateDesc(walletId, userId).stream()
                .map(financialTransactionModelMapper::mapFinancialTransactionEntityToFinancialTransactionDTO)
                .toList();
    }

    @Override
    public FinancialTransactionDTO findFinancialTransactionForUser(
            @Min(1) @NotNull Long financialTransactionId, Long userId) {
        FinancialTransaction financialTransaction = financialTransactionRepository
                .findByIdAndWalletUserId(financialTransactionId, userId)
                .orElseThrow(() -> new AppRuntimeException(ErrorCode.FT001,
                        String.format("Financial transaction with id: %d not found", financialTransactionId)));

        return financialTransactionModelMapper
                .mapFinancialTransactionEntityToFinancialTransactionDTO(financialTransaction);
    }

    @Override
    public void deleteTransactionById(@Min(1) @NotNull Long financialTransactionId, Long userId) {
        if (financialTransactionRepository.existsByIdAndWalletUserId(financialTransactionId, userId)) {
            financialTransactionRepository.deleteById(financialTransactionId);
        } else {
            throw new AppRuntimeException(
                    ErrorCode.FT001,
                    String.format("FinancialTransaction with given id: %d does not exist", financialTransactionId));
        }
    }

    @Override
    @Transactional
    public FinancialTransactionDTO updateFinancialTransaction(
            @Min(1) @NotNull Long financialTransactionId,
            @Valid FinancialTransactionUpdateDTO uDTO, Long userId) {

        FinancialTransaction ftEntity = financialTransactionRepository
                .findByIdAndWalletUserId(financialTransactionId, userId)
                .orElseThrow(() -> new AppRuntimeException(ErrorCode.FT001,
                        String.format("Financial transaction with id: %d not found", financialTransactionId)));

        FinancialTransactionCategory financialTransactionCategory = null;
        Long categoryId = uDTO.categoryId();

        if (categoryId != null) {
            financialTransactionCategory = financialTransactionCategoryRepository.findByIdAndUserId(categoryId, userId)
                    .orElseThrow(() -> new AppRuntimeException(ErrorCode.FTC001,
                            String.format("Financial transaction category with id: %d does not exist", categoryId)));

            if (uDTO.type() != financialTransactionCategory.getType()) {
                throw new AppRuntimeException(ErrorCode.FT002,
                        String.format("Financial transaction type: '%s' does not match with category type: '%s'",
                                uDTO.type(), financialTransactionCategory.getType()));
            }
        }

        ftEntity.setFinancialTransactionCategory(financialTransactionCategory);
        ftEntity.setType(uDTO.type());
        ftEntity.setAmount(uDTO.amount());
        ftEntity.setDescription(uDTO.description());
        ftEntity.setDate(uDTO.date());

        return financialTransactionModelMapper.mapFinancialTransactionEntityToFinancialTransactionDTO(ftEntity);
    }

    private FinancialTransactionCategory findFinancialTransactionCategory(Long categoryId, Long userId) {
        FinancialTransactionCategory financialTransactionCategory = null;

        if (categoryId != null) {
            financialTransactionCategory = financialTransactionCategoryRepository.findByIdAndUserId(categoryId, userId)
                    .orElseThrow(() -> {
                        throw new AppRuntimeException(ErrorCode.FTC001,
                                String.format("Financial transaction category with id: %d does not exist", categoryId));
                    });
        }
        return financialTransactionCategory;
    }

    private FinancialTransaction buildFinancialTransaction(FinancialTransactionCreateDTO financialTransactionCreateDTO,
                                                           Wallet wallet,
                                                           FinancialTransactionCategory financialTransactionCategory) {
        return FinancialTransaction.builder()
                .type(financialTransactionCreateDTO.type())
                .date(financialTransactionCreateDTO.date())
                .description(financialTransactionCreateDTO.description())
                .wallet(wallet)
                .amount(financialTransactionCreateDTO.amount())
                .financialTransactionCategory(financialTransactionCategory)
                .build();
    }

}
