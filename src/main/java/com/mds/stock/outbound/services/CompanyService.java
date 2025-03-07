package com.mds.stock.outbound.services;

import com.mds.stock.inbound.exceptions.EntityAlreadyExistsException;
import com.mds.stock.inbound.exceptions.EntityNotFoundException;
import com.mds.stock.outbound.dao.models.Company;
import com.mds.stock.outbound.dao.repositories.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public Company createCompany(Company company) {
        if (companyRepository.existsByNameAndCode(company.getName(), company.getCode())) {
            log.warn("Company with name: {} and code: {} already exists", company.getName(), company.getCode());
            throw new EntityAlreadyExistsException(String.format("Company with name: %s and code: %s already exists", company.getName(), company.getCode()));
        }

        log.info("Creating company: {}", company.getName());
        company.setId(null);

        return companyRepository.save(company);
    }

    @Transactional(readOnly = true)
    public Company getCompanyById(Integer id) {
        log.info("Extracting company with ID: {} from DB", id);
        return companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Company with id: %d not found", id)));
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public Company updateCompany(Company company) {
        if (companyRepository.existsById(company.getId())) {
            log.info("Updating company with id: {} and name: {}", company.getId(), company.getName());
            return companyRepository.save(company);
        }

        log.warn("Company with provided id: {} does not exist. Invalid request", company.getId());
        throw new EntityNotFoundException(String.format("Company with provided id: %d does not exist", company.getId()));
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public Boolean removeCompanyById(Integer id) {
        if (companyRepository.existsById(id)) {
            log.info("Removing company with id: {}", id);
            companyRepository.deleteById(id);
            return true;
        }

        log.warn("Company with provided id: {} does not exist and can't be removed", id);
        throw new EntityNotFoundException(String.format("Company with provided id: %d does not exist and can't be removed", id));
    }
}
