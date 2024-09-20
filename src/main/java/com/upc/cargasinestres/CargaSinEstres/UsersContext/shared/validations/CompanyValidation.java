package com.upc.cargasinestres.CargaSinEstres.UsersContext.shared.validations;

import com.upc.cargasinestres.CargaSinEstres.Shared.exception.ValidationException;
import com.upc.cargasinestres.CargaSinEstres.UsersContext.model.dto.Company.request.CompanyRequestDto;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The CompanyValidation class provides methods for validating CompanyRequestDto objects.
 * It checks for the length and presence of required fields in a company request.
 */
public class CompanyValidation {

    public static void validateCompanyName(String name) {
        if (name.length() > 30) {
            throw new ValidationException("El nombre no puede exceder los 30 caracteres");
        }
    }
    public static void validateCompanyLogo(String logo) {
        if (!logo.toLowerCase().startsWith("https://")) {
            throw new ValidationException("El logo debe ser una URL que comience con 'https://'");
        }
    }

    public static void validateCompanyDescription(String description) {
        if (description.length() > 250) {
            throw new ValidationException("La descripción no puede exceder los 250 caracteres");
        }
    }
    public static void validateCompanyTIC(String tic) {
        if (tic.length() != 11) {
            throw new ValidationException("El RUC debe tener exactamente 11 caracteres");
        }
        if (!tic.matches("\\d+")) {
            throw new ValidationException("El RUC debe ser un número");
        }
    }
    public static void validateCompanyPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() != 9) {
            throw new ValidationException("El teléfono debe tener exactamente 9 digitos");
        }
        if (!phoneNumber.matches("\\d+")) {
            throw new ValidationException("El teléfono debe ser un número");
        }
    }
    public static void validateCompanyPassword(String password) {
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
    public static void validateCompanyEmail(String email) {
        if (!email.matches("^(.+)@(.+)$")) {
            throw new ValidationException("El email debe ser válido");
        }
    }
    public static void validateCompanyDirection(String direction) {
        if (direction.length() > 250) {
            throw new ValidationException("La dirección no puede exceder los 250 caracteres");
        }
    }

    public static void validateCompanyServices(List<Long> servicioIds, List<Long> availableServicioIds) {
        long distinctServiceCount = servicioIds.stream().distinct().count();
        if (distinctServiceCount != servicioIds.size()) {
            throw new ValidationException("La empresa no puede ofrecer servicios duplicados");
        }

        List<Long> invalidServicioIds = servicioIds.stream()
                .filter(id -> !availableServicioIds.contains(id))
                .collect(Collectors.toList());

        if (!invalidServicioIds.isEmpty()) {
            System.out.println("invalidServicioIds: "+invalidServicioIds);

            throw new ValidationException("Este servicio no existe, solo puede acceder a los siguiente servicos: " + availableServicioIds);
        }

    }

    public static void ValidateCompany(CompanyRequestDto companyRequestDto, List<Long> availableServicioIds){

        if(companyRequestDto.getName().isEmpty()){
            throw new ValidationException("El nombre no puede estar vacio");
        }
        if(companyRequestDto.getLogo().isEmpty()){
            throw new ValidationException("El logo no puede estar vacio");
        }
        if(companyRequestDto.getDescription().isEmpty()){
            throw new ValidationException("La descripción no puede estar vacia");
        }
        if(companyRequestDto.getTIC().isEmpty()){
            throw new ValidationException("El RUC no puede estar vacio");
        }
        if(companyRequestDto.getPhoneNumber().isEmpty()){
            throw new ValidationException("El teléfono no puede estar vacio");
        }
        if(companyRequestDto.getEmail().isEmpty()){
            throw new ValidationException("El email no puede estar vacio");
        }
        if(companyRequestDto.getDirection().isEmpty()){
            throw new ValidationException("La dirección no puede estar vacia");
        }
        if(companyRequestDto.getPassword().isEmpty()){
            throw new ValidationException("La contraseña no puede estar vacia");
        }
        if (companyRequestDto.getServicioIds().isEmpty()) {
            throw new ValidationException("La empresa debe ofrecer al menos un servicio");
        }

        // Validaciones de longitud, formato y duplicados
        validateCompanyName(companyRequestDto.getName());
        validateCompanyLogo(companyRequestDto.getLogo());
        validateCompanyDescription(companyRequestDto.getDescription());
        validateCompanyTIC(companyRequestDto.getTIC());
        validateCompanyPhoneNumber(companyRequestDto.getPhoneNumber());
        validateCompanyEmail(companyRequestDto.getEmail());
        validateCompanyDirection(companyRequestDto.getDirection());
        validateCompanyPassword(companyRequestDto.getPassword());
        validateCompanyServices(companyRequestDto.getServicioIds(), availableServicioIds);
    }
}
