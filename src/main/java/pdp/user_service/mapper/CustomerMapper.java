package pdp.user_service.mapper;

import pdp.user_service.dto.CustomerDetailsDto;
import pdp.user_service.dto.CustomerDto;
import pdp.user_service.model.Customer;

import java.util.List;

public class CustomerMapper {

    public static CustomerDto toCustomerDto(Customer customer) {
        return new CustomerDto(customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone());
    }

    public static Customer toEntity(CustomerDto dto) {
        Customer customer = new Customer();
        customer.setId(customer.getId());
        customer.setFirstName(dto.firstName());
        customer.setLastName(dto.lastName());
        customer.setEmail(dto.email());
        customer.setPhone(dto.phone());
        return customer;
    }

    public static List<CustomerDto> toCustomerDtos(List<Customer> customers) {
        return customers.stream()
                .map(CustomerMapper::toCustomerDto).toList();
    }

    public static CustomerDetailsDto toCustomerDetailsDto(Customer customer) {
        return new CustomerDetailsDto(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getBankAccounts()
        );
    }
}
