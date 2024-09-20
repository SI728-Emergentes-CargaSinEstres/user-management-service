package com.upc.cargasinestres.CargaSinEstres.UsersContext.model.dto.Customer.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * The ClientRequestDto class represents the data transfer object of the Client class.
 * It contains fields related to the details of a client entity.
 * This class is used for client information when creating or updating a client.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;
    private LocalDate dateOfBirth;
}