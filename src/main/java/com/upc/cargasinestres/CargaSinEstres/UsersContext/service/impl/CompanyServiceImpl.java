package com.upc.cargasinestres.CargaSinEstres.UsersContext.service.impl;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.upc.cargasinestres.CargaSinEstres.Shared.exception.ResourceNotFoundException;
import com.upc.cargasinestres.CargaSinEstres.UsersContext.model.dto.Company.request.CompanyRequestDto;
import com.upc.cargasinestres.CargaSinEstres.UsersContext.model.dto.Company.response.CompanyResponseDto;
import com.upc.cargasinestres.CargaSinEstres.UsersContext.model.entity.Company;
import com.upc.cargasinestres.CargaSinEstres.UsersContext.repository.ICompanyRepository;
import com.upc.cargasinestres.CargaSinEstres.UsersContext.service.ICompanyService;
import com.upc.cargasinestres.CargaSinEstres.UsersContext.shared.validations.CompanyValidation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of the ICompanyService interface.
 * Handles the business logic for company operations.
 * @version 1.0*/
@Service
public class CompanyServiceImpl implements ICompanyService {

    private final ICompanyRepository companyRepository;
    private final ModelMapper modelMapper;
    private final RestTemplate restTemplate;

    //inyeccion de dependencias
    public CompanyServiceImpl(ICompanyRepository companyRepository, ModelMapper modelMapper, RestTemplate restTemplate) {

        this.companyRepository = companyRepository;
        this.modelMapper = modelMapper;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<CompanyResponseDto> getAllCompanies() {
        var companies = companyRepository.findAll();

        return companies.stream()
                .map(company -> {
                    CompanyResponseDto companyResponseDto = modelMapper.map(company, CompanyResponseDto.class);
                    int averageRating = calculateAverageRating(company.getId());
                    companyResponseDto.setAverageRating(averageRating);
                    return companyResponseDto;
                })
                .toList();
    }


    @Override
    public CompanyResponseDto getCompanyById(Long id) {
        var company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro la empresa con id: " + id));

        int averageRating = calculateAverageRating(company.getId());

        CompanyResponseDto companyResponseDto = modelMapper.map(company, CompanyResponseDto.class);
        companyResponseDto.setAverageRating(averageRating);

        return companyResponseDto;
    }

    @Override
    public CompanyResponseDto createCompany(CompanyRequestDto companyRequestDto) {
        if(companyRepository.findByName(companyRequestDto.getName()).isPresent())
            throw new RuntimeException("Ya existe una empresa con ese nombre");

        if(companyRepository.findByTIC(companyRequestDto.getTIC()).isPresent())
            throw new RuntimeException("Ya existe una empresa con ese RUC");

        if (companyRepository.findByEmail(companyRequestDto.getEmail()).isPresent())
            throw new RuntimeException("Ya existe una empresa con ese email");

        if (companyRepository.findByPhoneNumber(companyRequestDto.getPhoneNumber()).isPresent())
            throw new RuntimeException("Ya existe una empresa con ese número de teléfono");

        if(companyRepository.findByLogo(companyRequestDto.getLogo()).isPresent())
            throw new RuntimeException("Ya existe una empresa con ese logo");

        CompanyValidation.ValidateCompany(companyRequestDto, getServiceIds());

        List<Long> servicioIds = companyRequestDto.getServicioIds();

        List<String> servicios = getNamesOfServicesByIdList(servicioIds);

        var newCompany = modelMapper.map(companyRequestDto, Company.class);

        newCompany.setServicioIds(servicioIds);
        newCompany.setServicios(servicios);

        var createdCompany = companyRepository.save(newCompany);
        return modelMapper.map(createdCompany, CompanyResponseDto.class);
    }

    @Override
    public CompanyResponseDto updateCompany(Long id, CompanyRequestDto companyRequestDto){
        var company = companyRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No se encontró la empresa con id: "+id));

        //Validar que campos setear
        if(companyRequestDto.getName() != null) {
            CompanyValidation.validateCompanyName(companyRequestDto.getName());
            company.setName(companyRequestDto.getName());
        }
        if(companyRequestDto.getLogo() != null) {
            CompanyValidation.validateCompanyLogo(companyRequestDto.getLogo());
            company.setLogo(companyRequestDto.getLogo());
        }
        if(companyRequestDto.getDescription() != null) {
            CompanyValidation.validateCompanyDescription(companyRequestDto.getDescription());
            company.setDescription(companyRequestDto.getDescription());
        }
        if(companyRequestDto.getTIC() != null) {
            CompanyValidation.validateCompanyTIC(companyRequestDto.getTIC());
            company.setTIC(companyRequestDto.getTIC());
        }
        if(companyRequestDto.getPhoneNumber() != null) {
            CompanyValidation.validateCompanyPhoneNumber(companyRequestDto.getPhoneNumber());
            company.setPhoneNumber(companyRequestDto.getPhoneNumber());
        }
        if(companyRequestDto.getEmail() != null) {
            CompanyValidation.validateCompanyEmail(companyRequestDto.getEmail());
            company.setEmail(companyRequestDto.getEmail());
        }
        if(companyRequestDto.getDirection() != null) {
            CompanyValidation.validateCompanyDirection(companyRequestDto.getDirection());
            company.setDirection(companyRequestDto.getDirection());
        }
        if(companyRequestDto.getPassword() != null) {
            CompanyValidation.validateCompanyPassword(companyRequestDto.getPassword());
            company.setPassword(companyRequestDto.getPassword());
        }
        if(companyRequestDto.getServicioIds()!=null) {

            CompanyValidation.ValidateCompany(companyRequestDto, getServiceIds());
            List<Long> servicioIds = companyRequestDto.getServicioIds();
            company.setServicioIds(servicioIds);

            List<String> servicios = getNamesOfServicesByIdList(servicioIds);
            company.setServicios(servicios);
        }

        Company updatedCompany = companyRepository.save(company); // se guardan los cambios en la base de datos
        return modelMapper.map(updatedCompany, CompanyResponseDto.class); // se retorna un responseDTO con los datos del company actualizado
    }

    @Override
    public CompanyResponseDto getCompanyForLogin(String email, String password) {

        var company = companyRepository.findByEmailAndPassword(email, password); //se obtiene

        if (company.isEmpty())
            throw new ResourceNotFoundException("No existe una empresa con ese email y password"); // se valida

        return modelMapper.map(company, CompanyResponseDto.class); // se retorna un responseDTO con los datos del company
    }

    public int calculateAverageRating(Long companyId) {
        try {
            String url = "http://localhost:8010/api/v1/ratings/company/" + companyId;

            List<Map<String, Object>> ratings = restTemplate.getForObject(url, List.class);

            if (ratings != null && !ratings.isEmpty()) {
                double sum = 0;
                for (Map<String, Object> rating : ratings) {
                    sum += (Integer) rating.get("stars");
                }
                return (int) (sum / ratings.size());
            } else {
                return 0;
            }
        } catch (Exception e){
            return 0;
        }
    }

    public List<Long> getServiceIds() {
        String url = "http://localhost:8080/company-management-service/api/v1/services";
        List<Long> serviceIds = new ArrayList<>();

        try {
            String response = restTemplate.getForObject(url, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> services = objectMapper.readValue(response, new TypeReference<List<Map<String, Object>>>() {});

            for (Map<String, Object> service : services) {
                serviceIds.add(((Number) service.get("id")).longValue());
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }

        return serviceIds;
    }

    public List<String> getNamesOfServicesByIdList(List<Long> serviceIds) {
        String url = "http://localhost:8080/company-management-service/api/v1/services/";
        List<String> stringIds = serviceIds.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());
        String ids = String.join(",", stringIds);
        url += ids;

        List<String> names = new ArrayList<>();

        try {
            List<Map<String, Object>> services = restTemplate.getForObject(url, List.class);

            for (Map<String, Object> service : services) {
                String name = (String) service.get("name");
                names.add(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return names;
    }
}
