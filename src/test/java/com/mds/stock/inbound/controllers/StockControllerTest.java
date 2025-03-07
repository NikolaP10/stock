package com.mds.stock.inbound.controllers;

import com.mds.stock.inbound.dto.CompanyDto;
import com.mds.stock.inbound.dto.StockDetailsDto;
import com.mds.stock.inbound.dto.StockDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StockControllerTest {

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();
    @LocalServerPort
    private int port;

    private String uri() {
        return "http://localhost:" + port + "/stock";
    }

    @Test
    void createStock() {
        StockDto stockDto = StockDto.builder()
                .low(new BigDecimal("0.758393"))
                .high(new BigDecimal("0.778214"))
                .open(new BigDecimal("0.758571"))
                .close(new BigDecimal("0.773393"))
                .adjClose(new BigDecimal("0.681905"))
                .volume(new BigDecimal("145000000"))
                .date(new Date(System.currentTimeMillis()))
                .code("AAPL")
                .build();

        HttpEntity<StockDto> entity = new HttpEntity<>(stockDto, headers);
        ResponseEntity<StockDto> response = restTemplate.exchange(
                uri(),
                HttpMethod.POST, entity, StockDto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void getStockById() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<StockDto> response = restTemplate.exchange(
                uri() + "/5",
                HttpMethod.GET, entity, StockDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, response.getBody().getId());
    }

    @Test
    void getAllStocksByCode() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<List<StockDto>> response = restTemplate.exchange(
                uri() + "/all/NPC",
                HttpMethod.GET, entity, new ParameterizedTypeReference<List<StockDto>>() {
                });
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void updateStock() {
        StockDto stockDto = StockDto.builder()
                .id(1)
                .low(new BigDecimal("0.758393"))
                .high(new BigDecimal("0.778214"))
                .open(new BigDecimal("0.758571"))
                .close(new BigDecimal("0.773393"))
                .adjClose(new BigDecimal("0.681905"))
                .volume(new BigDecimal("145000000"))
                .date(new Date(System.currentTimeMillis()))
                .code("GOOG")
                .build();

        HttpEntity<StockDto> entity = new HttpEntity<>(stockDto, headers);
        ResponseEntity<StockDto> response = restTemplate.exchange(
                uri() + "/update",
                HttpMethod.PUT, entity, StockDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getId());
    }

    @Test
    void updateStock_EntityNotFound() {
        StockDto stockDto = StockDto.builder()
                .id(0)
                .low(new BigDecimal("0.758393"))
                .high(new BigDecimal("0.778214"))
                .open(new BigDecimal("0.758571"))
                .close(new BigDecimal("0.773393"))
                .adjClose(new BigDecimal("0.681905"))
                .volume(new BigDecimal("145000000"))
                .date(new Date(System.currentTimeMillis()))
                .code("GOOG")
                .build();

        HttpEntity<StockDto> entity = new HttpEntity<>(stockDto, headers);
        ResponseEntity<StockDto> response = restTemplate.exchange(
                uri() + "/update",
                HttpMethod.PUT, entity, StockDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void removeStock() {
        HttpEntity<Boolean> entity = new HttpEntity<>(null, headers);
        ResponseEntity<Boolean> response = restTemplate.exchange(
                uri() + "/1",
                HttpMethod.DELETE, entity, Boolean.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void getStockDetails() {
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<StockDetailsDto> response = restTemplate.exchange(
                uri() + "/details/AAPL?from=2005-09-10&to=2005-09-20",
                HttpMethod.GET, entity, StockDetailsDto.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getRequestedPeriod());
    }
}