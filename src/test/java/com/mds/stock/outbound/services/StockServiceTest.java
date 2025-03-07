package com.mds.stock.outbound.services;

import com.mds.stock.inbound.exceptions.EntityNotFoundException;
import com.mds.stock.outbound.dao.models.Stock;
import com.mds.stock.outbound.dao.repositories.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StockServiceTest {

    StockService stockService;

    @Mock
    StockRepository stockRepository;
    AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        stockService = new StockService(stockRepository);
    }

    @AfterEach
    void destroy() throws Exception {
        closeable.close();
    }

    private Stock buildStock() {
        Stock stock = new Stock();
        stock.setHigh(new BigDecimal("0.778214"));
        stock.setLow(new BigDecimal("0.758393"));
        stock.setOpen(new BigDecimal("0.758571"));
        stock.setClose(new BigDecimal("0.773393"));
        stock.setAdjClose(new BigDecimal("0.681905"));
        stock.setVolume(new BigDecimal("145000000"));
        stock.setDate(new Date(System.currentTimeMillis()));
        stock.setCode("GOOG");

        return stock;
    }

    @Test
    void createStock() {
        Stock stock = buildStock();

        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        Stock response = stockService.createStock(stock);
        assertNotNull(response);
        assertEquals(stock.getCode(), response.getCode());
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    void getAllStocksByCode() {
        List<Stock> stockList = Collections.singletonList(buildStock());

        when(stockRepository.findByCode(anyString())).thenReturn(stockList);

        List<Stock> response = stockService.getAllStocksByCode("NPC");
        assertEquals(1, stockList.size());
        verify(stockRepository, times(1)).findByCode(anyString());
    }

    @Test
    void getStockById() {
        Stock stock = buildStock();
        stock.setId(1);

        when(stockRepository.findById(anyInt())).thenReturn(Optional.of(stock));

        Stock response = stockService.getStockById(1);
        assertNotNull(response);
        assertEquals(1, response.getId());
        verify(stockRepository, times(1)).findById(anyInt());
    }

    @Test
    void updateStock_updated() {
        Stock stock = buildStock();
        stock.setId(1);

        when(stockRepository.existsByIdAndCode(anyInt(), anyString())).thenReturn(true);
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        Stock response = stockService.updateStock(stock);
        assertEquals(1, response.getId());
        verify(stockRepository, times(1)).existsByIdAndCode(anyInt(), anyString());
        verify(stockRepository, times(1)).save(any(Stock.class));
    }

    @Test
    void updateStock_notFound() {
        Stock stock = buildStock();
        stock.setId(1);

        when(stockRepository.existsByIdAndCode(anyInt(), anyString())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> stockService.updateStock(stock));

        verify(stockRepository, times(1)).existsByIdAndCode(anyInt(), anyString());
        verify(stockRepository, times(0)).save(any(Stock.class));
    }

    @Test
    void removeStockById_removed() {
        when(stockRepository.existsById(anyInt())).thenReturn(true);
        doNothing().when(stockRepository).deleteById(anyInt());

        assertTrue(stockService.removeStockById(1));

        verify(stockRepository, times(1)).existsById(anyInt());
        verify(stockRepository, times(1)).deleteById(anyInt());
    }

    @Test
    void removeStockById_notFound() {
        when(stockRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> stockService.removeStockById(1));

        verify(stockRepository, times(1)).existsById(anyInt());
        verify(stockRepository, times(0)).deleteById(anyInt());
    }

    @Test
    void getBestBuy() {
        Stock stock = buildStock();

        when(stockRepository.getBestBuyDate(anyString(), any(Date.class), any(Date.class))).thenReturn(stock);

        assertNotNull(stockService.getBestBuy("NPC", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())));
        verify(stockRepository, times(1)).getBestBuyDate(anyString(), any(Date.class), any(Date.class));
    }

    @Test
    void getBestSell() {
        Stock stock = buildStock();

        when(stockRepository.getBestSellDate(anyString(), any(Date.class), any(Date.class))).thenReturn(stock);

        assertNotNull(stockService.getBestSell("NPC", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())));
        verify(stockRepository, times(1)).getBestSellDate(anyString(), any(Date.class), any(Date.class));

    }

    @Test
    void getStockByCodeAndEndingDate() {
        Stock stock = buildStock();

        when(stockRepository.getStockByCodeAndEndingDate(anyString(), any(Date.class), any(Date.class))).thenReturn(stock);

        assertNotNull(stockService.getStockByCodeAndEndingDate("NPC", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())));
        verify(stockRepository, times(1)).getStockByCodeAndEndingDate(anyString(), any(Date.class), any(Date.class));
    }

    @Test
    void getStockByCodeAndStartingDate() {
        Stock stock = buildStock();

        when(stockRepository.getStockByCodeAndStartingDate(anyString(), any(Date.class), any(Date.class))).thenReturn(stock);

        assertNotNull(stockService.getStockByCodeAndStartingDate("NPC", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis())));
        verify(stockRepository, times(1)).getStockByCodeAndStartingDate(anyString(), any(Date.class), any(Date.class));

    }

    @Test
    void getStocksForPeriod() {
        List<Stock> stockList = Collections.singletonList(buildStock());

        when(stockRepository.getStocksForPeriod(anyString(), any(Date.class), any(Date.class))).thenReturn(stockList);
        List<Stock> response = stockService.getStocksForPeriod("NPC", new Date(System.currentTimeMillis()), new Date(System.currentTimeMillis()));

        assertEquals(1, response.size());
        verify(stockRepository, times(1)).getStocksForPeriod(anyString(), any(Date.class), any(Date.class));
    }

    @Test
    void getStockCodes() {
        List<String> codes = Arrays.asList("NPC", "AAPL", "GOOG");

        when(stockRepository.getAllStockCodes()).thenReturn(codes);
        List<String> response = stockService.getStockCodes();
        assertNotNull(response);
        assertEquals(3, response.size());

        verify(stockRepository, times(1)).getAllStockCodes();
    }
}