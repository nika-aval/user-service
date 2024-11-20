package pdp.user_service.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pdp.user_service.dto.BankAccountDto;
import pdp.user_service.model.BankAccount;
import pdp.user_service.model.Customer;
import pdp.user_service.repository.BankAccountRepository;
import pdp.user_service.repository.CustomerRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper mapper;

    public BankAccountService(BankAccountRepository bankAccountRepository, CustomerRepository customerRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.customerRepository = customerRepository;
        mapper = new ModelMapper();
    }

    @Transactional
    public BankAccountDto openNewBankAccount(Long customerId, BankAccountDto bankAccountDto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException(String.format("Customer with id %s not found", customerId)));
        BankAccount account = bankAccountRepository.save(mapper.map(bankAccountDto, BankAccount.class));
        account.setCustomer(customer);
        return mapper.map(account, BankAccountDto.class);
    }

    public List<BankAccountDto> getBankAccountsByCustomerId(Long customerId) {
        return bankAccountRepository.findAllByCustomerId(customerId).stream()
                .map(bankAccount -> mapper.map(bankAccount, BankAccountDto.class))
                .toList();
    }

    @Transactional
    public BankAccountDto deposit(String iban,  BigDecimal amount) {
        BankAccount account = bankAccountRepository.findByIban(iban)
                .orElseThrow(() -> new RuntimeException(String.format("IBAN %s not found", iban)));
        account.setBalance(account.getBalance().add(amount));
        return mapper.map(account, BankAccountDto.class);
    }

    @Transactional
    public BankAccountDto withdraw(String iban,  BigDecimal amount) {
        BankAccount account = bankAccountRepository.findByIban(iban)
                .orElseThrow(() -> new RuntimeException(String.format("IBAN %s not found", iban)));

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance!");
        }
        account.setBalance(account.getBalance().subtract(amount));
        return mapper.map(account, BankAccountDto.class);
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
