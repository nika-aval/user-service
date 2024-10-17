package pdp.user_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pdp.user_service.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
