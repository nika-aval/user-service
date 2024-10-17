package pdp.user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pdp.user_service.dto.BankAccountDto;
import pdp.user_service.model.BankAccount;
import pdp.user_service.model.Customer;
import pdp.user_service.repository.BankAccountRepository;
import pdp.user_service.repository.CustomerRepository;

import java.math.BigDecimal;
import java.util.List;

import static pdp.user_service.mapper.BankAccountMapper.*;

@Service
@RequiredArgsConstructor
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public BankAccountDto openNewBankAccount(Long customerId, BankAccountDto bankAccountDto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException(String.format("Customer with id %s not found", customerId)));

        BankAccount account = bankAccountRepository.save(toEntity(bankAccountDto));
        account.setCustomer(customer);
        return toDto(account);
    }

    public List<BankAccountDto> getBankAccountsByCustomerId(Long customerId) {
        return toDtos(bankAccountRepository.findAllByCustomerId(customerId));
    }

    @Transactional
    public BankAccountDto deposit(String iban,  BigDecimal amount) {
        BankAccount account = bankAccountRepository.findByIban(iban)
                .orElseThrow(() -> new RuntimeException(String.format("IBAN %s not found", iban)));
        account.setBalance(account.getBalance().add(amount));
        return toDto(account);
    }

    @Transactional
    public BankAccountDto withdraw(String iban,  BigDecimal amount) {
        BankAccount account = bankAccountRepository.findByIban(iban)
                .orElseThrow(() -> new RuntimeException(String.format("IBAN %s not found", iban)));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance!");
        }
        account.setBalance(account.getBalance().subtract(amount));
        return toDto(account);
    }

    @Transactional
    public String transfer(String ibanFrom, String ibanTo,  BigDecimal amount) {
        BankAccount from = bankAccountRepository.findByIban(ibanFrom)
                .orElseThrow(() -> new RuntimeException(String.format("IBAN %s not found", ibanFrom)));
        BankAccount to = bankAccountRepository.findByIban(ibanTo)
                .orElseThrow(() -> new RuntimeException(String.format("IBAN %s not found", ibanTo)));

        if (from.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance!");
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        return String.format("Money has successfully transferred from %s to %s", from.getIban(), to.getIban());
    }

}
