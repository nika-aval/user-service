package pdp.user_service.mapper;

import pdp.user_service.dto.BankAccountDto;
import pdp.user_service.model.BankAccount;

import java.util.List;

public class BankAccountMapper {

    public static BankAccountDto toDto(BankAccount bankAccount) {
        return new BankAccountDto(bankAccount.getId(), bankAccount.getIban(), bankAccount.getBalance());
    }

    public static BankAccount toEntity(BankAccountDto bankAccountDto) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setActive(true);
        bankAccount.setIban(bankAccountDto.iban());
        bankAccount.setBalance(bankAccountDto.balance());
        return bankAccount;
    }

    public static List<BankAccountDto> toDtos(List<BankAccount> bankAccounts) {
        return bankAccounts.stream().map(BankAccountMapper::toDto).toList();
    }
}
