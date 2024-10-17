package pdp.user_service.dto;

import pdp.user_service.model.BankAccount;

import java.util.Set;

public record CustomerDetailsDto(Long id, String firstName,
                                 String lastName, String email,
                                 String phone, Set<BankAccount> bankAccounts) {
}
