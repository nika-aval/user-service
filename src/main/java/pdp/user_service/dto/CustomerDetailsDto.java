package pdp.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pdp.user_service.model.BankAccount;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDetailsDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    Set<BankAccount> bankAccounts;
}