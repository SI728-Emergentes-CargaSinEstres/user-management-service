package com.upc.cargasinestres.CargaSinEstres.UsersContext.model.dto.Customer.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * The ClientResponseDto class represents the data transfer object of the Client class.
 * It contains fields related to the details of a client entity.
 * This class is used for client information when retrieving a client.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private LocalDate dateOfBirth;
}
