package pdp.user_service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String iban;
    private BigDecimal balance = BigDecimal.ZERO;
    private boolean isActive = true;
    @JsonIgnore
    @ManyToOne
    private Customer customer;
}
