package com.upc.cargasinestres.CargaSinEstres.UsersContext.service.impl;

import com.upc.cargasinestres.CargaSinEstres.Shared.exception.ResourceNotFoundException;
import com.upc.cargasinestres.CargaSinEstres.UsersContext.model.dto.Customer.request.CustomerRequestDto;
import com.upc.cargasinestres.CargaSinEstres.UsersContext.model.dto.Customer.response.CustomerResponseDto;
import com.upc.cargasinestres.CargaSinEstres.UsersContext.model.entity.Customer;
import com.upc.cargasinestres.CargaSinEstres.UsersContext.repository.ICustomerRepository;
import com.upc.cargasinestres.CargaSinEstres.UsersContext.service.ICustomerService;
import com.upc.cargasinestres.CargaSinEstres.UsersContext.shared.validations.CustomerValidation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

/**
 * Implementation of the IClientService interface.
 * Handles the business logic for client operations.
 * */
@Service
public class CustomerServiceImpl implements ICustomerService {

    private final ICustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    //inyeccion de dependencias
    public CustomerServiceImpl(ICustomerRepository customerRepository, ModelMapper modelMapper) {
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * Creates a new customer.
     *
     * @param customerRequestDto The data for creating the client.
     * @return The created client response.
     * @throws RuntimeException if a client with the same email and password already exists.
     */
    @Override
    public CustomerResponseDto createCustomer(CustomerRequestDto customerRequestDto) {

        if(customerRepository.findByPhoneNumber(customerRequestDto.getPhoneNumber()).isPresent())
            throw new RuntimeException("Ya existe un cliente con ese número de teléfono");

        if(customerRepository.findByEmail(customerRequestDto.getEmail()).isPresent())
            throw new RuntimeException("Ya existe un cliente con ese email");

        CustomerValidation.ValidateCustomer(customerRequestDto);

        var newCustomer = modelMapper.map(customerRequestDto, Customer.class);
        /*
        newCustomer.setUserType("customer");
         */
        var createdCustomer = customerRepository.save(newCustomer);
        return modelMapper.map(createdCustomer, CustomerResponseDto.class);
    }

    /**
     * Retrieves a list of clients for login based on email and password.
     *
     * @param email    The customer's email.
     * @param password The customer's password.
     * @return A list of client response DTOs.
     */
    @Override
    public CustomerResponseDto getCustomerForLogin(String email, String password) {
        var customer = customerRepository.findByEmailAndPassword(email, password);
        if (customer == null)
            throw new ResourceNotFoundException("No existe una empresa con ese email y password"); // se valida

        return modelMapper.map(customer, CustomerResponseDto.class);
    }


    @Override
    public CustomerResponseDto getCustomerById(Long id) {
        var customer = customerRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No se encontro el cliente con id: "+id));
        return modelMapper.map(customer, CustomerResponseDto.class);
    }

    @Override
    public CustomerResponseDto updateCustomer(Long id, CustomerRequestDto customerRequestDto){
        var customer = customerRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No se encontro el cliente con id: "+id));

        //Validar que campos setear
        if(customerRequestDto.getFirstName() != null) {
            CustomerValidation.validateCustomerName(customerRequestDto.getFirstName());
            customer.setFirstName(customerRequestDto.getFirstName());
        }
        if(customerRequestDto.getLastName() != null) {
            CustomerValidation.validateCustomerLastName(customerRequestDto.getLastName());
            customer.setLastName(customerRequestDto.getLastName());
        }
        if(customerRequestDto.getDateOfBirth() != null) {
            CustomerValidation.validateCustomerDateOfBirth(customerRequestDto.getDateOfBirth());
            customer.setDateOfBirth(customerRequestDto.getDateOfBirth());
        }
        if(customerRequestDto.getEmail() != null) {
            CustomerValidation.validateCustomerEmail(customerRequestDto.getEmail());
            customer.setEmail(customerRequestDto.getEmail());
        }
        if(customerRequestDto.getPhoneNumber() != null) {
            CustomerValidation.validateCustomerPhoneNumber(customerRequestDto.getPhoneNumber());
            customer.setPhoneNumber(customerRequestDto.getPhoneNumber());
        }
        if(customerRequestDto.getPassword() != null) {
            CustomerValidation.validateCustomerPassword(customerRequestDto.getPassword());
            customer.setPassword(customerRequestDto.getPassword());
        }

        Customer updateCustomer = customerRepository.save(customer);
        return modelMapper.map(updateCustomer, CustomerResponseDto.class);
    }


}