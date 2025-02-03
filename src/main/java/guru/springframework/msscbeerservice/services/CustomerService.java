package guru.springframework.msscbeerservice.services;


import guru.sfg.brewery.model.CustomerDto;

import java.util.UUID;

public interface CustomerService {
    CustomerDto getCustomerById(UUID beerId);

    CustomerDto saveCustomer(CustomerDto customerDto);

    CustomerDto updateCustomer(UUID customerId, CustomerDto customerDto);

    void deleteCustomer(UUID customerId);
}

