package pdp.user_service.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pdp.user_service.dto.BankAccountDto;
import pdp.user_service.dto.CustomerDto;
import pdp.user_service.repository.BankAccountRepository;
import pdp.user_service.repository.CustomerRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class BankAccountServiceIT {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private BankAccountRepository bankAccountRepository;
    private CustomerService customerService;
    private BankAccountService bankAccountService;

    @BeforeEach
    @AfterEach
    void setUp() {
        customerService = new CustomerService(customerRepository);
        bankAccountService = new BankAccountService(bankAccountRepository, customerRepository);
        bankAccountRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void shouldOpenNewBankAccount() {
        // GIVEN
        Long customerId = customerService.registerCustomer(generateCustomerDto()).id();
        BankAccountDto expectedBankAccount = generateBankAccountDto();

        // WHEN
        BankAccountDto actualBankAccount = bankAccountService.openNewBankAccount(customerId, expectedBankAccount);

        // THEN
        assertThat(actualBankAccount)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedBankAccount);
    }

    @Test
    void shouldGetBankAccountsByCustomerId() {
        // GIVEN
        Long customerId = customerService.registerCustomer(generateCustomerDto()).id();
        BankAccountDto bankAccountDto = bankAccountService.openNewBankAccount(customerId, generateBankAccountDto());

        // WHEN
        List<BankAccountDto> bankAccounts = bankAccountService.getBankAccountsByCustomerId(customerId);

        assertThat(bankAccounts.size())
                .isEqualTo(1);
        assertThat(bankAccounts.getFirst())
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(bankAccountDto);
    }

    @Test
    void shouldDeposit() {
        // GIVEN
        Long customerId = customerService.registerCustomer(generateCustomerDto()).id();
        bankAccountService.openNewBankAccount(customerId, generateBankAccountDto());

        // WHEN
        BankAccountDto accountWithBalance = bankAccountService.deposit("GE25BG0011223344", BigDecimal.valueOf(2550));

        // THEN
        assertThat(accountWithBalance.balance())
                .isEqualTo(BigDecimal.valueOf(12550));
    }

    @Test
    void shouldWithdraw() {
        // GIVEN
        Long customerId = customerService.registerCustomer(generateCustomerDto()).id();
        bankAccountService.openNewBankAccount(customerId, generateBankAccountDto());

        // WHEN
        BankAccountDto accountWithBalance = bankAccountService.withdraw("GE25BG0011223344", BigDecimal.valueOf(2000));

        // THEN
        assertThat(accountWithBalance.balance())
                .isEqualTo(BigDecimal.valueOf(8000));
    }

    @Test
    void shouldTransfer() {
        // GIVEN
        Long customerId = customerService.registerCustomer(generateCustomerDto()).id();
        bankAccountService.openNewBankAccount(customerId, generateBankAccountDto());
        bankAccountService.openNewBankAccount(customerId, new BankAccountDto(null, "GE25BG5566778899", BigDecimal.ZERO));

        // WHEN
        bankAccountService.transfer("GE25BG0011223344", "GE25BG5566778899", BigDecimal.valueOf(3500));
        List<BankAccountDto> bankAccountsByCustomerId = bankAccountService.getBankAccountsByCustomerId(customerId);

        // THEN
        assertThat(bankAccountsByCustomerId.size())
                .isEqualTo(2);
        assertThat(bankAccountsByCustomerId.get(0).balance())
                .isEqualTo(BigDecimal.valueOf(6500));
        assertThat(bankAccountsByCustomerId.get(1).balance())
                .isEqualTo(BigDecimal.valueOf(3500));
    }

    @Test
    void shouldThrowExceptionWhenNotEnoughBalanceWhileTransferring() {
        // GIVEN
        Long customerId = customerService.registerCustomer(generateCustomerDto()).id();
        bankAccountService.openNewBankAccount(customerId, generateBankAccountDto());
        bankAccountService.openNewBankAccount(customerId, new BankAccountDto(null, "GE25BG5566778899", BigDecimal.ZERO));

        // WHEN && THEN
        assertThatThrownBy(() -> bankAccountService.transfer("GE25BG0011223344", "GE25BG5566778899", BigDecimal.valueOf(33500)))
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessageContaining("Insufficient balance!");
    }

    private CustomerDto generateCustomerDto() {
        return new CustomerDto(null,
                "Nika",
                "Avalishvili",
                "myemail@gmail.com",
                "+995-000-111-222");
    }

    private BankAccountDto generateBankAccountDto() {
        return new BankAccountDto(null,
                "GE25BG0011223344",
                BigDecimal.valueOf(10000));
    }
}
