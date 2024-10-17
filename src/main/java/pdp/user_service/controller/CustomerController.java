package pdp.user_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pdp.user_service.dto.CustomerDetailsDto;
import pdp.user_service.dto.CustomerDto;
import pdp.user_service.service.CustomerService;

import java.util.List;

@Tag(name = "Customer", description = "APIs for managing customers")
@RestController
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    @Operation(summary = "Register a new customer", description = "Creates a new customer with the provided details")
    public CustomerDto registerCustomer(@RequestBody @Parameter(description = "Customer details") CustomerDto customerDto) {
        return customerService.registerCustomer(customerDto);
    }

    @PutMapping("/update/{customerId}")
    @Operation(summary = "Update an existing customer", description = "Updates a customer with the provided details")
    public CustomerDto updateCustomer(@PathVariable @Parameter(description = "Customer ID") Long customerId,
                                      @RequestBody @Parameter(description = "Customer details") CustomerDto customerDto) {
        return customerService.updateCustomer(customerId, customerDto);
    }

    @GetMapping("/{customerId}")
    @Operation(summary = "Get specific customer with details", description = "Retrieves customer details by ID")
    public CustomerDetailsDto getCustomer(@PathVariable @Parameter(description = "Customer ID") Long customerId) {
        return customerService.getCustomerDetails(customerId);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all customers with details", description = "Retrieves all customers from database")
    public List<CustomerDto> getAllCustomers() {
        return customerService.getAllCustomers();
    }
}

