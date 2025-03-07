package com.mds.stock.outbound.dao.repositories;

import com.mds.stock.outbound.dao.models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface StockRepository extends JpaRepository<Stock, Integer> {

    boolean existsByIdAndCode(Integer id, String code);

    List<Stock> findByCode(String code);

    @Query("select s from Stock s where s.code=?1 and s.date between ?2 and ?3 order by low asc limit 1")
    Stock getBestBuyDate(String code, Date from, Date to);

    @Query("select s from Stock s where s.code=?1 and s.date between ?2 and ?3 order by high desc limit 1")
    Stock getBestSellDate(String code, Date from, Date to);

    @Query("select s from Stock s where s.code=?1 and s.date between ?2 and ?3 order by date desc limit 1")
    Stock getStockByCodeAndEndingDate(String code, Date from, Date to);

    @Query("select s from Stock s where s.code=?1 and s.date between ?2 and ?3 order by date asc limit 1")
    Stock getStockByCodeAndStartingDate(String code, Date from, Date to);

    @Query("select s from Stock s where s.code=?1 and s.date between ?2 and ?3 order by date asc")
    List<Stock> getStocksForPeriod(String code, Date from, Date to);

    @Query("select distinct(code) from Stock")
    List<String> getAllStockCodes();
}
