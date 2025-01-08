package guru.springframework.msscbeerservice.web.mappers;

import guru.springframework.msscbeerservice.domain.Customer;
import guru.springframework.msscbeerservice.web.model.CustomerDto;
import org.mapstruct.Mapper;

@Mapper
public interface CustomerMapper {

    CustomerDto customerToCustomerDto(Customer customer);
    Customer customerDtoToCustomer(CustomerDto customerDto);
}
