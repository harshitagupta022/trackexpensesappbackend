package pl.harshita_gupta.trackexpensesappbackend.financialtransaction.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.harshita_gupta.trackexpensesappbackend.financialtransaction.api.model.FinancialTransaction;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialTransactionRepository extends JpaRepository<FinancialTransaction, Long> {

    List<FinancialTransaction> findAllByWalletIdOrderByDateDesc(Long id);

    List<FinancialTransaction> findAllByWalletIdAndWalletUserIdOrderByDateDesc(Long walletId, Long userId);

    BigInteger countFinancialTransactionsByFinancialTransactionCategoryId(Long financialTransactionCategoryId);

    Optional<FinancialTransaction> findByIdAndWalletUserId(Long id, Long userId);

    Boolean existsByIdAndWalletUserId(Long financialTransactionId, Long userId);

}
