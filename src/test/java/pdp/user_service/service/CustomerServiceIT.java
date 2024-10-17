package pdp.user_service.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import pdp.user_service.dto.CustomerDetailsDto;
import pdp.user_service.dto.CustomerDto;
import pdp.user_service.repository.CustomerRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static pdp.user_service.mapper.CustomerMapper.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class CustomerServiceIT {

    @Autowired
    private CustomerRepository customerRepository;
    private CustomerService customerService;

    @BeforeEach
    @AfterEach
    void setUp() {
        customerService = new CustomerService(customerRepository);
        customerRepository.deleteAll();
    }

    @Test
    void shouldRegisterCustomer() {
        // GIVEN
        CustomerDto expectedCustomer = generateCustomerDto();

        // WHEN
        CustomerDto actualCustomer = customerService.registerCustomer(expectedCustomer);

        //THEN
        assertThat(expectedCustomer)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualCustomer);
    }

    @Test
    void shouldUpdateCustomer() {
        // GIVEN
        CustomerDto customerOld = generateCustomerDto();
        Long customerId = customerRepository.save(toEntity(customerOld)).getId();
        CustomerDto expectedCustomer = new CustomerDto(null, "Harry", "Potter", "myemail@gmail.com", "+995-000-111-222");

        // WHEN
        CustomerDto actualCustomer = customerService.updateCustomer(customerId, expectedCustomer);

        //THEN
        assertThat(expectedCustomer)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualCustomer);
    }

    @Test
    void shouldNotUpdateAndTrowExceptionWhenWrongIdProvided() {
        // GIVEN
        CustomerDto expectedCustomer = generateCustomerDto();

        // WHEN && THEN
        assertThatThrownBy(() -> customerService.updateCustomer(99L, expectedCustomer))
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessageContaining("Customer with id 99 not found!");
    }

    @Test
    void shouldGetCustomerDetails() {
        // GIVEN
        CustomerDto customer = generateCustomerDto();
        CustomerDetailsDto expectedCustomer = toCustomerDetailsDto(customerRepository.save(toEntity(customer)));

        // WHEN
        CustomerDetailsDto actualCustomer = customerService.getCustomerDetails(expectedCustomer.id());

        //THEN
        assertThat(expectedCustomer)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualCustomer);
    }

    @Test
    void shouldTrowExceptionWhenWrongIdProvided() {
        // GIVEN && WHEN && THEN
        assertThatThrownBy(() -> customerService.getCustomerDetails(99L))
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessageContaining("Customer with id 99 not found!");
        assertThatThrownBy(() -> customerService.getCustomer(99L))
                .isExactlyInstanceOf(RuntimeException.class)
                .hasMessageContaining("Customer with id 99 not found!");
    }

    @Test
    void shouldGetCustomer() {
        // GIVEN
        CustomerDto expectedCustomer = customerService.registerCustomer(generateCustomerDto());

        // WHEN
        CustomerDto actualCustomer = customerService.getCustomer(expectedCustomer.id());

        //THEN
        assertThat(expectedCustomer)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualCustomer);
    }

    @Test
    void shouldGetAllCustomers() {
        // GIVEN
        CustomerDto customer1 = generateCustomerDto();
        CustomerDto customer2 = new CustomerDto(null, "Giorgi", "Shengelia", "otheremail@gmail.com", "+995-333-444-555");
        List<CustomerDto> expectedCustomerDtos = toCustomerDtos(customerRepository.saveAll(List.of(toEntity(customer1), toEntity(customer2))));

        // WHEN
        List<CustomerDto> actualCustomers = customerService.getAllCustomers();

        // THEN
        assertThat(actualCustomers).hasSize(2);
        assertThat(actualCustomers)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expectedCustomerDtos);
    }

    private CustomerDto generateCustomerDto() {
        return new CustomerDto(null,
                "Nika",
                "Avalishvili",
                "myemail@gmail.com",
                "+995-000-111-222");
    }
}