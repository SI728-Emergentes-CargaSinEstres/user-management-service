package com.upc.cargasinestres.CargaSinEstres.UsersContext.model.dto.Company.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * The CompanyResponseDto class represents the data transfer object of the Company class.
 * It contains fields related to the details of a company entity.
 * This class is used for company information when retrieving a company.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyResponseDto {
    private Long id;
    private String name;
    private String TIC;
    private String direction;
    private String email;
    private String phoneNumber;
    private String description;
    private String logo;
    private List<String> servicios;
    private int averageRating;
}