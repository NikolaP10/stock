package com.mds.stock.inbound.controllers;

import com.mds.stock.inbound.dto.CompanyDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CompanyControllerTest {

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();
    @LocalServerPort
    private int port;

    private String uri() {
        return "http://localhost:" + port + "/company";
    }

    @Test
    void createCompany() {
        CompanyDto companyDto = CompanyDto.builder()
                .address("Street 123")
                .city("New York")
                .owner("Mark Twain")
                .country("USA")
                .code("SPC")
                .name("Solid Company")
                .founded(new Date(System.currentTimeMillis()))
                .build();

        HttpEntity<CompanyDto> entity = new HttpEntity<>(companyDto, headers);
        ResponseEntity<CompanyDto> response = restTemplate.exchange(
                uri(),
                HttpMethod.POST, entity, CompanyDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void createCompany_AlreadyExists() {
        CompanyDto companyDto = CompanyDto.builder()
                .address("Street 123")
                .city("New York")
                .owner("Mark Twain")
                .country("USA")
                .code("SPC")
                .name("Solid Company")
                .founded(new Date(System.currentTimeMillis()))
                .build();

        HttpEntity<CompanyDto> entity = new HttpEntity<>(companyDto, headers);
        ResponseEntity<CompanyDto> response = restTemplate.exchange(
                uri(),
                HttpMethod.POST, entity, CompanyDto.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getCompanyById() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<CompanyDto> response = restTemplate.exchange(
                uri() + "/1",
                HttpMethod.GET, entity, CompanyDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
    }

    @Test
    void getCompanyById_EntityNotFound() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<CompanyDto> response = restTemplate.exchange(
                uri() + "/0",
                HttpMethod.GET, entity, CompanyDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateCompany() {
        CompanyDto companyDto = CompanyDto.builder()
                .id(1)
                .address("Street 123")
                .city("New York")
                .owner("Mark Twain")
                .country("USA")
                .code("SPC")
                .name("Solid Company")
                .founded(new Date(System.currentTimeMillis()))
                .build();

        HttpEntity<CompanyDto> entity = new HttpEntity<>(companyDto, headers);
        ResponseEntity<CompanyDto> response = restTemplate.exchange(
                uri() + "/update",
                HttpMethod.PUT, entity, CompanyDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
    }

    @Test
    void removeCompany() {
        HttpEntity<Boolean> entity = new HttpEntity<>(null, headers);
        ResponseEntity<Boolean> response = restTemplate.exchange(
                uri() + "/1",
                HttpMethod.DELETE, entity, Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }
}