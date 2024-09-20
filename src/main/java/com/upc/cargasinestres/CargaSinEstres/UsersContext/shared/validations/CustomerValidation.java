package com.upc.cargasinestres.CargaSinEstres.UsersContext.shared.validations;


import com.upc.cargasinestres.CargaSinEstres.Shared.exception.ValidationException;
import com.upc.cargasinestres.CargaSinEstres.UsersContext.model.dto.Customer.request.CustomerRequestDto;

import java.time.LocalDate;

/**
 * The CustomerValidation class provides methods for validating CustomerRequestDto objects.
 * It checks for the length and presence of required fields in a customer request.
 */
public class CustomerValidation {
    public static void validateCustomerName(String name) {
        if (name.length() > 20) {
            throw new ValidationException("El nombre no puede exceder los 20 caracteres");
        }
    }
    public static void validateCustomerLastName(String lastName) {
        if (lastName.length() > 20) {
            throw new ValidationException("El apellido no puede exceder los 20 caracteres");
        }
    }
    public static void validateCustomerEmail(String email) {
        if (!email.matches("^(.+)@(.+)$")) {
            throw new ValidationException("El email debe ser válido");
        }
    }
    public static void validateCustomerPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() != 9) {
            throw new ValidationException("El celular debe tener exactamente 9 digitos");
        }
        if (!phoneNumber.matches("\\d+")) {
            throw new ValidationException("El celular debe contener solo dígitos enteros");
        }
    }
    public static void validateCustomerDateOfBirth(LocalDate dateOfBirth) {
        LocalDate currentDate = LocalDate.now(); // Calcular la fecha actual
        if (dateOfBirth.isAfter(currentDate) ) {
            throw new ValidationException("La fecha de nacimiento no puede ser en el futuro"); // ERROR 400
        }

        LocalDate eighteenYearsAgo = currentDate.minusYears(17); // Calcular la fecha hace 18 años (validacion con un año menos)
        if (dateOfBirth.isAfter(eighteenYearsAgo)) { // Comprobar si la fecha de nacimiento es posterior a la fecha hace 18 años
            throw new RuntimeException("No puede crear una cuenta si es menor de edad");
        }
    }
    public static void validateCustomerPassword(String password) {
        if (password.length() < 8 ) {
            throw new ValidationException("La contraseña debe tener al menos 8 caracteres");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new ValidationException("La contraseña debe contener al menos una letra mayúscula");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new ValidationException("La contraseña debe contener al menos una letra minúscula");
        }
        if (!password.matches(".*\\d.*")) {
            throw new ValidationException("La contraseña debe contener al menos un número");
        }
    }

    public static void ValidateCustomer(CustomerRequestDto customerRequestDto){
        if(customerRequestDto.getFirstName().isEmpty()){
            throw new ValidationException("El nombre debe ser obligatorio");
        }
        if(customerRequestDto.getLastName().isEmpty()) {
            throw new ValidationException("El apellido debe ser obligatorio");
        }
        if (customerRequestDto.getDateOfBirth() == null) {
            throw new ValidationException("La fecha de nacimiento debe ser obligatoria"); // ERROR 400
        }
        if (customerRequestDto.getEmail().isEmpty()) {
            throw new ValidationException("El email debe ser obligatorio");
        }
        if (customerRequestDto.getPhoneNumber().isEmpty()) {
            throw new ValidationException("El celular debe ser obligatorio");
        }
        if (customerRequestDto.getPassword().isEmpty()) {
            throw new ValidationException("La contraseña debe ser obligatoria");
        }

        // Validaciones de longitud, formato y duplicados
        validateCustomerName(customerRequestDto.getFirstName());
        validateCustomerLastName(customerRequestDto.getLastName());
        validateCustomerEmail(customerRequestDto.getEmail());
        validateCustomerPhoneNumber(customerRequestDto.getPhoneNumber());
        validateCustomerDateOfBirth(customerRequestDto.getDateOfBirth());
        validateCustomerPassword(customerRequestDto.getPassword());
    }
}