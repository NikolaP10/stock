package com.mds.stock.inbound.controllers;

import com.mds.stock.inbound.dto.CompanyDto;
import com.mds.stock.inbound.mappers.CompanyMapper;
import com.mds.stock.outbound.dao.models.Company;
import com.mds.stock.outbound.services.CompanyService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/company")
@Produces(MediaType.APPLICATION_JSON)
public class CompanyController {

    // should be invoked through companyManager, ideally, but for the simplicity, we're using it directly
    private final CompanyService companyService;
    private final CompanyMapper companyMapper;

    @PostMapping
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<CompanyDto> createCompany(@RequestBody CompanyDto companyDto) {
        log.info("Request received to create company with name: {} and code: {}", companyDto.getName(), companyDto.getCode());
        Company company = companyMapper.fromDtoToEntity(companyDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(companyMapper.fromEntityToDto(companyService.createCompany(company)));
    }

    @GetMapping("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<CompanyDto> getCompanyById(@PathVariable Integer id) {
        log.info("Request received to retrieve company with id: {}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(companyMapper.fromEntityToDto(companyService.getCompanyById(id)));
    }

    @PutMapping("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<CompanyDto> updateCompany(@RequestBody CompanyDto companyDto) {
        log.info("Request received to update company with name: {} and code: {}", companyDto.getName(), companyDto.getCode());
        Company company = companyMapper.fromDtoToEntity(companyDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(companyMapper.fromEntityToDto(companyService.updateCompany(company)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> removeCompany(@PathVariable Integer id) {
        log.info("Request received to remove company with id: {}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(companyService.removeCompanyById(id));
    }
}
