package guru.springframework.msscbeerservice.services;


import guru.springframework.msscbeerservice.web.model.CustomerDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService{
    @Override
    public CustomerDto getCustomerById(UUID customerId) {
        return CustomerDto.builder()
                .id(customerId)
                .name("random customer")
                .build();
    }

    @Override
    public CustomerDto saveCustomer(CustomerDto customerDto) {
        customerDto.setId(UUID.randomUUID());
        return customerDto;
    }

    @Override
    public CustomerDto updateCustomer(UUID customerId, CustomerDto customerDto) {
        return customerDto;
    }

    @Override
    public void deleteCustomer(UUID customerId) {

    }
}
