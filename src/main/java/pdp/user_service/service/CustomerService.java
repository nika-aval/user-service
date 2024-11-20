package pdp.user_service.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pdp.user_service.dto.CustomerDetailsDto;
import pdp.user_service.dto.CustomerDto;
import pdp.user_service.model.Customer;
import pdp.user_service.repository.CustomerRepository;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final ModelMapper mapper;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        mapper = new ModelMapper();
    }

    public CustomerDto registerCustomer(CustomerDto customerDto) {
        Customer customer = mapper.map(customerDto, Customer.class);
        return mapper.map(customerRepository.save(customer), CustomerDto.class);
    }

    @Transactional
    public CustomerDto updateCustomer(Long customerId, CustomerDto customerDto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException(String.format("Customer with id %s not found!", customerId)));

        if (customerDto.getFirstName() != null || !customerDto.getFirstName().isBlank()) {
            customer.setFirstName(customerDto.getFirstName());
        }

        if (customerDto.getLastName() != null || !customerDto.getLastName().isBlank()) {
            customer.setLastName(customerDto.getLastName());
        }

        if (customerDto.getEmail() != null || !customerDto.getEmail().isBlank()) {
            customer.setEmail(customerDto.getEmail());
        }

        if (customerDto.getPhone() != null || !customerDto.getPhone().isBlank()) {
            customer.setPhone(customerDto.getPhone());
        }

        return mapper.map(customer, CustomerDto.class);
    }

    public CustomerDetailsDto getCustomerDetails(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException(String.format("Customer with id %s not found!", customerId)));
        return mapper.map(customer, CustomerDetailsDto.class);
    }

    public CustomerDto getCustomer(Long customerId) {
        return mapper.map(customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException(String.format("Customer with id %s not found!", customerId))), CustomerDto.class);
    }

    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customer -> mapper.map(customer, CustomerDto.class))
                .toList();
    }
}
