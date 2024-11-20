package pdp.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pdp.user_service.model.BankAccount;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    List<BankAccount> findAllByCustomerId(Long customerId);

    Optional<BankAccount> findByIban(String iban);

}
