package com.mds.stock.outbound.services;

import com.mds.stock.inbound.exceptions.EntityNotFoundException;
import com.mds.stock.outbound.dao.models.Stock;
import com.mds.stock.outbound.dao.repositories.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class StockService {

    private final StockRepository stockRepository;

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public Stock createStock(Stock stock) {
        log.info("Creating a new stock for company: {}", stock.getCode());
        stock.setId(null);
        return stockRepository.save(stock);
    }

    @Transactional(readOnly = true)
    public List<Stock> getAllStocksByCode(String code) {
        log.info("Retrieving all stocks for company: {}", code);
        return stockRepository.findByCode(code);
    }

    @Transactional(readOnly = true)
    public Stock getStockById(Integer id) {
        log.info("Retrieving stock with id: {}", id);
        return stockRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Stock with id: %d not found", id)));
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public Stock updateStock(Stock stock) {
        if (stockRepository.existsByIdAndCode(stock.getId(), stock.getCode())) {
            log.info("Updating stock with id: {} and code: {}", stock.getId(), stock.getCode());
            return stockRepository.save(stock);
        }

        log.warn("Stock with provided id: {} does not exist for company with code: {}. Invalid request", stock.getId(), stock.getCode());
        throw new EntityNotFoundException(String.format("Stock with provided id: %d for company with code:%s does not exist", stock.getId(), stock.getCode()));
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public Boolean removeStockById(Integer id) {
        if (stockRepository.existsById(id)) {
            log.info("Removing stock with id: {}", id);
            stockRepository.deleteById(id);
            return true;
        }

        log.warn("Stock with provided id: {} does not exist and can't be removed", id);
        throw new EntityNotFoundException(String.format("Stock with provided id: %d does not exist and can't be removed", id));
    }

    @Transactional(readOnly = true)
    public Stock getBestBuy(String code, Date from, Date to) {
        return stockRepository.getBestBuyDate(code, from, to);
    }

    @Transactional(readOnly = true)
    public Stock getBestSell(String code, Date from, Date to) {
        return stockRepository.getBestSellDate(code, from, to);
    }

    @Transactional(readOnly = true)
    public Stock getStockByCodeAndEndingDate(String code, Date from, Date to) {
        return stockRepository.getStockByCodeAndEndingDate(code, from, to);
    }

    @Transactional(readOnly = true)
    public Stock getStockByCodeAndStartingDate(String code, Date from, Date to) {
        return stockRepository.getStockByCodeAndStartingDate(code, from, to);
    }

    @Transactional(readOnly = true)
    public List<Stock> getStocksForPeriod(String code, Date from, Date to) {
        return stockRepository.getStocksForPeriod(code, from, to);
    }

    @Transactional(readOnly = true)
    public List<String> getStockCodes() {
        return stockRepository.getAllStockCodes();
    }
}
