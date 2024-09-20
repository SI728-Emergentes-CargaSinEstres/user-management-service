package com.upc.cargasinestres.CargaSinEstres.UsersContext.controller;

import com.upc.cargasinestres.CargaSinEstres.UsersContext.model.dto.Company.request.CompanyRequestDto;
import com.upc.cargasinestres.CargaSinEstres.UsersContext.model.dto.Company.response.CompanyResponseDto;
import com.upc.cargasinestres.CargaSinEstres.UsersContext.service.ICompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Company controller
 * @author Grupo1
 * @version 1.0
 * */
@Tag(name="Company Controller")
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1")
public class CompanyController {

    private final ICompanyService companyService;

    /**
     * Constructs a new CompanyController with the specified company service.
     * @param companyService The service responsible for handling company-related operations.
     */
    public CompanyController(ICompanyService companyService) {
        this.companyService = companyService;
    }

    /**
     * Retrieves a list of all companies.
     * @return A ResponseEntity containing a list of CompanyResponseDto and HttpStatus OK.
     */
    @Operation(summary = "Get all companies")
    @GetMapping("/companies")
    public ResponseEntity<List<CompanyResponseDto>> getAllCompanies() {
        var res = companyService.getAllCompanies();
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Retrieves a specific company by its unique identifier.
     * @param id The unique identifier of the company.
     * @return A ResponseEntity containing the CompanyResponseDto for the specified ID and HttpStatus OK.
     */
    @Operation(summary = "Get a company by Id")
    @GetMapping("/companies/{id}")
    public ResponseEntity<CompanyResponseDto> getCompanyById(@PathVariable Long id) {
        var res = companyService.getCompanyById(id);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @Operation(summary = "Get a company for login")
    @GetMapping("/companiesForLogin")
    public ResponseEntity<CompanyResponseDto> getCompanyForLogin(@RequestParam(name="email") String email, @RequestParam(name="password")String password){
        var res = companyService.getCompanyForLogin(email, password);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    /**
     * Registers a new company based on the provided data.
     * @param companyRequestDto The data for registering the company.
     * @return A ResponseEntity containing the registered CompanyResponseDto and HttpStatus CREATED.
     */
    @Operation(summary = "Registers a Company")
    @PostMapping("/companies")
    public ResponseEntity<CompanyResponseDto> createCompany(@RequestBody CompanyRequestDto companyRequestDto) {
        var res = companyService.createCompany(companyRequestDto);
        return new ResponseEntity<>(res, HttpStatus.CREATED);
    }

    /**
     * Updates an existing company based on the provided data and unique identifier.
     * @param id The unique identifier of the company to be updated.
     * @param companyRequestDto The data for updating the company.
     * @return A ResponseEntity containing the updated CompanyResponseDto and HttpStatus OK.
     */
    @Operation(summary = "Update a Company")
    @PatchMapping("/companies/{id}")
    public ResponseEntity<CompanyResponseDto> updateCompany(@PathVariable Long id, @RequestBody CompanyRequestDto companyRequestDto){
        var res = companyService.updateCompany(id, companyRequestDto);
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
