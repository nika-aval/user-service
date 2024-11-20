package pdp.user_service.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pdp.user_service.dto.CustomerDetailsDto;
import pdp.user_service.dto.CustomerDto;
import pdp.user_service.model.Customer;
import pdp.user_service.repository.CustomerRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerServiceIT {

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerService customerService;

    private final ModelMapper mapper = new ModelMapper();

    @BeforeEach
    @AfterEach
    void setUp() {
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
        Long customerId = customerRepository.save(mapper.map(customerOld, Customer.class)).getId();
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
    @Transactional
    void shouldGetCustomerDetails() {
        // GIVEN
        CustomerDto customer = generateCustomerDto();
        CustomerDetailsDto expectedCustomer = mapper.map(customerRepository.save(mapper.map(customer, Customer.class)), CustomerDetailsDto.class);

        // WHEN
        CustomerDetailsDto actualCustomer = customerService.getCustomerDetails(expectedCustomer.getId());

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
        CustomerDto actualCustomer = customerService.getCustomer(expectedCustomer.getId());

        //THEN
        assertThat(expectedCustomer)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(actualCustomer);
    }

//    @Test
//    void shouldGetAllCustomers() {
//        // GIVEN
//        CustomerDto customer1 = generateCustomerDto();
//        CustomerDto customer2 = new CustomerDto(null, "Giorgi", "Shengelia", "otheremail@gmail.com", "+995-333-444-555");
//        List<CustomerDto> expectedCustomerDtos = customerRepository.saveAll(List.of(mapper.map(customer1, Customer.class), mapper.map(customer2, Customer.class))).stream()
//                .map(customer -> mapper.map(customer, CustomerDto.class))
//                .toList();
//
//        // WHEN
//        List<CustomerDto> actualCustomers = customerService.getAllCustomers();
//
//        // THEN
//        assertThat(actualCustomers).hasSize(2);
//        assertThat(actualCustomers)
//                .usingRecursiveComparison()
//                .ignoringFields("id")
//                .isEqualTo(expectedCustomerDtos);
//    }

    private CustomerDto generateCustomerDto() {
        return new CustomerDto(null,
                "Nika",
                "Avalishvili",
                "myemail@gmail.com",
                "+995-000-111-222");
    }
}