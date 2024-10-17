package pdp.user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pdp.user_service.dto.CustomerDetailsDto;
import pdp.user_service.dto.CustomerDto;
import pdp.user_service.model.Customer;
import pdp.user_service.repository.CustomerRepository;

import java.util.List;

import static pdp.user_service.mapper.CustomerMapper.*;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerDto registerCustomer(CustomerDto customerDto) {
        Customer customer = toEntity(customerDto);
        return toCustomerDto(customerRepository.save(customer));
    }

    @Transactional
    public CustomerDto updateCustomer(Long customerId, CustomerDto customerDto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException(String.format("Customer with id %s not found!", customerId)));

        if (customerDto.firstName() != null || !customerDto.firstName().isBlank()){
            customer.setFirstName(customerDto.firstName());
        }

        if (customerDto.lastName() != null || !customerDto.lastName().isBlank()){
            customer.setLastName(customerDto.lastName());
        }

        if (customerDto.email() != null || !customerDto.email().isBlank()){
            customer.setEmail(customerDto.email());
        }

        if (customerDto.phone() != null || !customerDto.phone().isBlank()){
            customer.setPhone(customerDto.phone());
        }

        return toCustomerDto(customer);
    }

    public CustomerDetailsDto getCustomerDetails(Long customerId) {
        return toCustomerDetailsDto(customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException(String.format("Customer with id %s not found!", customerId))));
    }

    public CustomerDto getCustomer(Long customerId) {
        return toCustomerDto(customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException(String.format("Customer with id %s not found!", customerId))));
    }

    public List<CustomerDto> getAllCustomers() {
        return toCustomerDtos(customerRepository.findAll());
    }
}
