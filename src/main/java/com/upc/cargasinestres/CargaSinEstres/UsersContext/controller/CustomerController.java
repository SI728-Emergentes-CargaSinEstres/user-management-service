package com.upc.cargasinestres.CargaSinEstres.UsersContext.controller;
import com.upc.cargasinestres.CargaSinEstres.UsersContext.model.dto.Customer.request.CustomerRequestDto;
import com.upc.cargasinestres.CargaSinEstres.UsersContext.model.dto.Customer.response.CustomerResponseDto;
import com.upc.cargasinestres.CargaSinEstres.UsersContext.service.ICustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Customer controller
 */
@Tag(name="Customer Controller")
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1")
public class CustomerController {

    private final ICustomerService customerService;

    /**
     * Class constructor
     * @param customerService The service for handling customer operations.
     */
    public CustomerController(ICustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Retrieves a list of customers for login based on the provided email and password.
     *
     * @param email The email of the customer.
     * @param password The password of the customer.
     * @return A ResponseEntity containing the list of customerResponseDto and HttpStatus.OK.
     */
    @Operation(summary = "Get customers for login")
    @GetMapping("/customers")
    public ResponseEntity<CustomerResponseDto> getCustomerForLogin(@RequestParam(name="email") String email, @RequestParam(name="password")String password){
        var res = customerService.getCustomerForLogin(email, password);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Retrieves a customer by their ID.
     *
     * @param id The ID of the customer.
     * @return A ResponseEntity containing the CustomerResponseDto and HttpStatus.OK.
     */
    @Operation(summary = "Get a customer by Id")
    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable Long id){
        var res = customerService.getCustomerById(id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Creates a new customer based on the provided customer data.
     *
     * @param customerRequestDto The data for creating the customer.
     * @return A ResponseEntity containing the created CustomerResponseDto and HttpStatus.CREATED.
     */
    @Operation(summary = "Create a Customer")
    @PostMapping("/customers")
    public ResponseEntity<CustomerResponseDto> createCustomer(@RequestBody CustomerRequestDto customerRequestDto) {
        var res = customerService.createCustomer(customerRequestDto);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    /**
     * Updates an existing customer based on the provided customer data.
     *
     * @param id The ID of the customer to be updated.
     * @param customerRequestDto The updated data for the customer.
     * @return A ResponseEntity containing the updated CustomerResponseDto and HttpStatus.OK.
     */
    @Operation(summary = "Update a Customer")
    @PatchMapping("/customers/{id}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(@PathVariable(name="id") Long id, @RequestBody CustomerRequestDto customerRequestDto){
        var res = customerService.updateCustomer(id, customerRequestDto);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}