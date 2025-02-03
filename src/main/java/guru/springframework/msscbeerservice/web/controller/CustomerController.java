package guru.springframework.msscbeerservice.web.controller;


import guru.springframework.msscbeerservice.services.CustomerService;
import guru.springframework.msscbeerservice.services.CustomerServiceImpl;
import guru.sfg.brewery.model.CustomerDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RequestMapping("/api/v1/customer")
@RestController
public class CustomerController {
    private final CustomerServiceImpl customerServiceImpl;
    private CustomerService customerService;


    public CustomerController(CustomerService customerService, CustomerServiceImpl customerServiceImpl) {
        this.customerService = customerService;
        this.customerServiceImpl = customerServiceImpl;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerDto> getCustomer(@PathVariable UUID customerId){
        return new ResponseEntity<>(customerService.getCustomerById(customerId), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto){
        CustomerDto savedCustomer = customerService.saveCustomer(customerDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(customerDto.getId())
                .toUri();

        return ResponseEntity.created(location).body(savedCustomer);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<CustomerDto> updateCustomer(@PathVariable UUID customerId,@RequestBody CustomerDto customerDto){
        CustomerDto updatedCustomer=customerService.updateCustomer(customerId,customerDto);

        return new ResponseEntity<>(updatedCustomer,HttpStatus.OK);
    }

    @DeleteMapping("/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable UUID customerId){
        customerServiceImpl.deleteCustomer(customerId);
    }
}
