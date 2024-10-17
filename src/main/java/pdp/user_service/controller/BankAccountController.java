package pdp.user_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pdp.user_service.dto.BankAccountDto;
import pdp.user_service.service.BankAccountService;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Bank Accounts", description = "APIs for managing bank accounts")
@RestController
@RequestMapping("/bank")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @PostMapping("/open-new-account/{customerId}")
    @Operation(summary = "Open a new bank account", description = "Creates a new bank account for a customer")
    public BankAccountDto openNewBankAccount(
            @PathVariable @Parameter(description = "Customer ID") Long customerId,
            @RequestBody @Parameter(description = "Bank account details") BankAccountDto bankAccountDto) {
        return bankAccountService.openNewBankAccount(customerId, bankAccountDto);
    }

    @GetMapping("/get-all-bank-accounts/{customerId}")
    @Operation(summary = "Get all bank accounts for a customer", description = "Retrieves a list of bank accounts for a customer")
    public List<BankAccountDto> getBankAccountsByCustomerId(@PathVariable @Parameter(description = "Customer ID") Long customerId) {
        return bankAccountService.getBankAccountsByCustomerId(customerId);
    }

    @PutMapping("/deposit")
    @Operation(summary = "Deposit money into a bank account", description = "Deposits a specified amount into a bank account")
    public BankAccountDto deposit(
            @Parameter(description = "IBAN of the bank account") String iban,
            @Parameter(description = "Amount to deposit") BigDecimal amount) {
        return bankAccountService.deposit(iban, amount);
    }

    @PutMapping("/withdraw")
    @Operation(summary = "Withdraw money from a bank account", description = "Withdraws a specified amount from a bank account")
    public BankAccountDto withdraw(
            @Parameter(description = "IBAN of the bank account") String iban,
            @Parameter(description = "Amount to withdraw") BigDecimal amount) {
        return bankAccountService.withdraw(iban, amount);
    }

    @PutMapping("/transfer")
    @Operation(summary = "Transfer money between bank accounts", description = "Transfers a specified amount from one bank account to another")
    public String transfer(
            @Parameter(description = "IBAN of the source bank account") String ibanFrom,
            @Parameter(description = "IBAN of the target bank account") String ibanTo,
            @Parameter(description = "Amount to transfer") BigDecimal amount) {
        return bankAccountService.transfer(ibanFrom, ibanTo, amount);
    }

}
