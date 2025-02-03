package guru.springframework.msscbeerservice.web.mappers;

import guru.springframework.msscbeerservice.domain.Customer;
import guru.sfg.brewery.model.CustomerDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface CustomerMapper {

    CustomerDto customerToCustomerDto(Customer customer);
    Customer customerDtoToCustomer(CustomerDto customerDto);
}
