package com.mds.stock.domain.managers;

import com.mds.stock.inbound.dto.StockDetailsDto;
import com.mds.stock.outbound.dao.models.Stock;
import com.mds.stock.outbound.services.StockService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class StockManagerTest {

    StockManager stockManager;
    @Mock
    StockService stockService;
    @Mock
    Stock mockStock;
    @Mock
    List<Stock> mockStockList;

    AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        stockManager = new StockManager(stockService);

        when(mockStock.getId()).thenReturn(1);
    }

    @AfterEach
    void destroy() throws Exception {
        closeable.close();
    }

    @Test
    void createStock() {
        when(stockService.createStock(any(Stock.class))).thenReturn(mockStock);
        assertNotNull(stockManager.createStock(mockStock));
        verify(stockService, times(1)).createStock(any(Stock.class));
    }

    @Test
    void getStockById() {
        when(stockService.getStockById(anyInt())).thenReturn(mockStock);
        assertEquals(1, stockManager.getStockById(1).getId());
        verify(stockService, times(1)).getStockById(anyInt());
    }

    @Test
    void getAllStocksByCode() {
        when(stockService.getAllStocksByCode(anyString())).thenReturn(Collections.singletonList(mockStock));
        assertEquals(1, stockManager.getAllStocksByCode("NPC").size());
        verify(stockService, times(1)).getAllStocksByCode(anyString());
    }

    @Test
    void updateStock() {
        when(stockService.updateStock(any(Stock.class))).thenReturn(mockStock);
        assertNotNull(stockManager.updateStock(mockStock));
        verify(stockService, times(1)).updateStock(any(Stock.class));
    }

    @Test
    void removeStockById() {
        when(stockService.removeStockById(anyInt())).thenReturn(true);
        assertTrue(stockManager.removeStockById(1));
        verify(stockService, times(1)).removeStockById(anyInt());
    }

    private Stock buildBestBuyStock(String code) {
        Stock stock = new Stock();
        stock.setHigh(new BigDecimal("0.778214"));
        stock.setLow(new BigDecimal("0.658393"));
        stock.setOpen(new BigDecimal("0.668571"));
        stock.setClose(new BigDecimal("0.673393"));
        stock.setAdjClose(new BigDecimal("0.681905"));
        stock.setVolume(new BigDecimal("145000000"));
        stock.setDate(new Date(System.currentTimeMillis()));
        stock.setCode(code);

        return stock;
    }

    private Stock buildBestSellStock(String code) {
        Stock stock = new Stock();
        stock.setHigh(new BigDecimal("0.778214"));
        stock.setLow(new BigDecimal("0.658393"));
        stock.setOpen(new BigDecimal("0.658571"));
        stock.setClose(new BigDecimal("0.773393"));
        stock.setAdjClose(new BigDecimal("0.681905"));
        stock.setVolume(new BigDecimal("145000000"));
        stock.setDate(new Date(System.currentTimeMillis()));
        stock.setCode(code);

        return stock;
    }

    private Stock buildStartDateStock(String code) {
        Stock stock = new Stock();
        stock.setHigh(new BigDecimal("0.778214"));
        stock.setLow(new BigDecimal("0.658393"));
        stock.setOpen(new BigDecimal("0.648571"));
        stock.setClose(new BigDecimal("0.663393"));
        stock.setAdjClose(new BigDecimal("0.681905"));
        stock.setVolume(new BigDecimal("145000000"));
        stock.setDate(new Date(System.currentTimeMillis()));
        stock.setCode(code);

        return stock;
    }

    private Stock buildEndDateStock(String code) {
        Stock stock = new Stock();
        stock.setHigh(new BigDecimal("0.778214"));
        stock.setLow(new BigDecimal("0.658393"));
        stock.setOpen(new BigDecimal("0.648571"));
        stock.setClose(new BigDecimal("0.673393"));
        stock.setAdjClose(new BigDecimal("0.681905"));
        stock.setVolume(new BigDecimal("145000000"));
        stock.setDate(new Date(System.currentTimeMillis()));
        stock.setCode(code);

        return stock;
    }

    @Test
    void getStockDetails() {
        // requested period stocks
        Stock bestBuyRequestedStock = buildBestBuyStock("AAPL");
        Stock bestSellRequestedStock = buildBestSellStock("AAPL");

        Stock startDateRequestedStock = buildStartDateStock("AAPL");
        Stock endDateRequestedStock = buildEndDateStock("AAPL");

        // requested period best buy (1 & 2)
        when(stockService.getBestBuy(eq("AAPL"), any(Date.class), any(Date.class))).thenReturn(bestBuyRequestedStock);
        when(stockService.getBestSell(eq("AAPL"), any(Date.class), any(Date.class))).thenReturn(bestSellRequestedStock);

        // requested period start - end date stock (3)
        when(stockService.getStockByCodeAndStartingDate(eq("AAPL"), any(Date.class), any(Date.class)))
                .thenReturn(startDateRequestedStock);
        when(stockService.getStockByCodeAndEndingDate(eq("AAPL"), any(Date.class), any(Date.class)))
                .thenReturn(endDateRequestedStock);

        // requested period max profit (4)
        when(stockService.getStocksForPeriod(eq("AAPL"), any(Date.class), any(Date.class)))
                .thenReturn(Arrays.asList(bestBuyRequestedStock, bestSellRequestedStock, startDateRequestedStock, endDateRequestedStock));

        // requested period others max profit (5)
        Stock googleStock1 = buildBestBuyStock("GOOG");
        googleStock1.setClose(new BigDecimal("0.873393"));
        Stock googleStock2 = buildBestSellStock("GOOG");
        Stock googleStock3 = buildStartDateStock("GOOG");
        Stock googleStock4 = buildEndDateStock("GOOG");
        googleStock4.setClose(new BigDecimal("0.793393"));
        when(stockService.getStockCodes()).thenReturn(Collections.singletonList("GOOG"));
        when(stockService.getStocksForPeriod(eq("GOOG"), any(Date.class), any(Date.class)))
                .thenReturn(Arrays.asList(googleStock1, googleStock2, googleStock3, googleStock4));

        // previous & following period (1 & 2)
        when(stockService.getBestBuy(eq("GOOG"), any(Date.class), any(Date.class))).thenReturn(googleStock1)
                .thenReturn(googleStock2).thenReturn(googleStock3).thenReturn(googleStock4);


        // 2020-03-07  -> 2020-03-15
        StockDetailsDto response = stockManager.getStockDetails("AAPL", new Date(1583574555000L), new Date(1583488155000L));
        assertNotNull(response);
        assertNotNull(response.getRequestedPeriod().getOthersMaxProfit().get("GOOG"));
    }
}