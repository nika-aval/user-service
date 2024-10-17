package pdp.user_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private Set<BankAccount> bankAccounts = new HashSet<>();

}