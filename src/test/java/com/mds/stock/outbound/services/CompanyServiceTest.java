package com.mds.stock.outbound.services;

import com.mds.stock.inbound.exceptions.EntityAlreadyExistsException;
import com.mds.stock.inbound.exceptions.EntityNotFoundException;
import com.mds.stock.outbound.dao.models.Company;
import com.mds.stock.outbound.dao.repositories.CompanyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CompanyServiceTest {

    CompanyService companyService;
    @Mock
    CompanyRepository companyRepository;
    AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        companyService = new CompanyService(companyRepository);
    }

    @AfterEach
    void destroy() throws Exception {
        closeable.close();
    }

    private Company buildCompany() {
        Company company = new Company();
        company.setCity("Belgrade");
        company.setCode("NPC");
        company.setName("Bad Company");
        company.setCountry("Serbia");
        company.setAddress("Belgrade 123");
        company.setFounded(new Date(System.currentTimeMillis()));
        company.setOwner("John Doe");
        return company;
    }

    @Test
    void createCompany_alreadyExist() {
        Company company = buildCompany();

        when(companyRepository.existsByNameAndCode(anyString(), anyString())).thenReturn(true);

        assertThrows(EntityAlreadyExistsException.class, () -> companyService.createCompany(company));
    }

    @Test
    void createCompany_created() {
        Company company = buildCompany();

        when(companyRepository.existsByNameAndCode(anyString(), anyString())).thenReturn(false);
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        Company response = companyService.createCompany(company);
        assertNotNull(response);
        assertEquals(company.getCode(), response.getCode());
        verify(companyRepository, times(1)).existsByNameAndCode(anyString(), anyString());
        verify(companyRepository, times(1)).save(any(Company.class));
    }

    @Test
    void getCompanyById_notFound() {
        when(companyRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> companyService.getCompanyById(1));
    }

    @Test
    void getCompanyById_found() {
        Company company = buildCompany();
        company.setId(1);
        when(companyRepository.findById(anyInt())).thenReturn(Optional.of(company));
        Company response = companyService.getCompanyById(1);
        assertNotNull(response);
        assertEquals(1, response.getId());
        verify(companyRepository, times(1)).findById(anyInt());

    }

    @Test
    void updateCompany_updated() {
        Company company = buildCompany();
        company.setId(1);

        when(companyRepository.existsById(anyInt())).thenReturn(true);
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        Company response = companyService.updateCompany(company);
        assertNotNull(response);
        assertEquals(company.getId(), response.getId());
        verify(companyRepository, times(1)).existsById(anyInt());
        verify(companyRepository, times(1)).save(any(Company.class));
    }

    @Test
    void updateCompany_notFound() {
        Company company = buildCompany();
        company.setId(0);

        when(companyRepository.existsById(anyInt())).thenReturn(false);
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        assertThrows(EntityNotFoundException.class, () -> companyService.updateCompany(company));
        verify(companyRepository, times(1)).existsById(anyInt());
        verify(companyRepository, times(0)).save(any(Company.class));
    }

    @Test
    void removeCompanyById_removed() {
        when(companyRepository.existsById((anyInt()))).thenReturn(true);
        doNothing().when(companyRepository).deleteById(anyInt());
        assertTrue(companyService.removeCompanyById(1));
    }

    @Test
    void removeCompanyById_companyNotExists() {
        when(companyRepository.existsById((anyInt()))).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> companyService.removeCompanyById(1));
    }
}