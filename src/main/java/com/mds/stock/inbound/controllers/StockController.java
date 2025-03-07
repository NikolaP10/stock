package com.mds.stock.inbound.controllers;

import com.mds.stock.inbound.dto.StockDetailsDto;
import com.mds.stock.inbound.dto.StockDto;
import com.mds.stock.inbound.mappers.StockMapper;
import com.mds.stock.domain.managers.StockManager;
import com.mds.stock.outbound.dao.models.Stock;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/stock")
@Produces(MediaType.APPLICATION_JSON)
public class StockController {

    private final StockManager stockManager;
    private final StockMapper stockMapper;

    @PostMapping
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<StockDto> createStock(@RequestBody StockDto stockDto) {
        log.info("Request received to create a new stock for company: {}", stockDto.getCode());
        Stock stock = stockMapper.fromDtoToEntity(stockDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(stockMapper.fromEntityToDto(stockManager.createStock(stock)));
    }

    @GetMapping("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<StockDto> getStockById(@PathVariable Integer id) {
        log.info("Request received to retrieve stock with id: {}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(stockMapper.fromEntityToDto(stockManager.getStockById(id)));
    }

    @GetMapping("/all/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<List<StockDto>> getAllStocksByCode(@PathVariable String code) {
        log.info("Request received to retrieve all stocks for company: {}", code);
        return ResponseEntity.status(HttpStatus.OK)
                .body(stockMapper.fromEntityListToDtoList(stockManager.getAllStocksByCode(code)));
    }

    @PutMapping("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<StockDto> updateStock(@RequestBody StockDto stockDto) {
        log.info("Request received to update stock with id: {} and code: {}", stockDto.getId(), stockDto.getCode());
        Stock stock = stockMapper.fromDtoToEntity(stockDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(stockMapper.fromEntityToDto(stockManager.updateStock(stock)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> removeStock(@PathVariable Integer id) {
        log.info("Request received to remove stock with id: {}", id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(stockManager.removeStockById(id));
    }

    @GetMapping("/details/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<StockDetailsDto> getStockDetails(@PathVariable String code, @RequestParam("from") Date from, @RequestParam("to") Date to) {
        log.info("Retrieving stocks for company code: {} for the period from: {} to: {}", code, from, to);

        return ResponseEntity.status(HttpStatus.OK)
                .body(stockManager.getStockDetails(code, from, to));
    }
}
