package com.mds.stock.domain.managers;

import com.mds.stock.inbound.dto.*;
import com.mds.stock.outbound.dao.models.Stock;
import com.mds.stock.outbound.services.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockManager {

    private final StockService stockService;
    public static final String PREVIOUS_PERIOD = "PREVIOUS_PERIOD";
    public static final String FOLLOWING_PERIOD = "FOLLOWING_PERIOD";

    public Stock createStock(Stock stock) {
        return stockService.createStock(stock);
    }

    public Stock getStockById(Integer id) {
        return stockService.getStockById(id);
    }

    public List<Stock> getAllStocksByCode(String code) {
        return stockService.getAllStocksByCode(code);
    }

    public Stock updateStock(Stock stock) {
        return stockService.updateStock(stock);
    }

    public Boolean removeStockById(Integer id) {
        return stockService.removeStockById(id);
    }

    public StockDetailsDto getStockDetails(String code, Date from, Date to) {
        // requested period
        // best buy/sell (point 1 & 2)
        StockDetailsForPeriod detailsRequestedPeriod = getStockDetailsForPeriod(code, from, to);

        // profit (point 3 diff in CLOSE between starting and ending date)
        Stock stockStartingDate = stockService.getStockByCodeAndStartingDate(code, from, to);
        Stock stockEndingDate = stockService.getStockByCodeAndEndingDate(code, from, to);
        BigDecimal closeDiff = stockStartingDate == null ? null : stockStartingDate.getClose().subtract(stockEndingDate.getClose());

        // point 4
        BigDecimal maxProfit = calculateMaxProfit(code, from, to);

        // point 5
        List<String> stockCodes = stockService.getStockCodes();
        Map<String, BigDecimal> otherCodesProfit = new HashMap<>();
        for (String stockCode : stockCodes) {
            BigDecimal codeMaxProfit = calculateMaxProfit(stockCode, from, to);
            if (maxProfit.compareTo(codeMaxProfit) < 0) {
                otherCodesProfit.put(stockCode, codeMaxProfit);
            }
        }

        // analysis for the current period
        StockDetailsAnalysisDto analysisDto = StockDetailsAnalysisDto.builder()
                .trade(detailsRequestedPeriod)
                .closeDiff(closeDiff)
                .maxProfit(maxProfit)
                .othersMaxProfit(otherCodesProfit)
                .build();

        // best buy/sell point 1 & 2
        // previous period
        Pair<Date, Date> previousPeriodPair = getPeriodPair(from, to, StockManager.PREVIOUS_PERIOD);
        StockDetailsForPeriod detailsPreviousPeriod = getStockDetailsForPeriod(code, previousPeriodPair.getFirst(), previousPeriodPair.getSecond());
        // following period
        Pair<Date, Date> followingPeriodPair = getPeriodPair(from, to, StockManager.FOLLOWING_PERIOD);
        StockDetailsForPeriod detailsFollowingPeriod = getStockDetailsForPeriod(code, followingPeriodPair.getFirst(), followingPeriodPair.getSecond());

        return StockDetailsDto.builder()
                .requestedPeriod(analysisDto)
                .previousPeriod(detailsPreviousPeriod)
                .followingPeriod(detailsFollowingPeriod)
                .build();
    }

    // max profit (since no detailed instructions provided for this calculation,
    // I'm taking into account that BUY=OPEN price, and SELL=CLOSE price ,
    // even though, during a day, you could buy it with lower price and sell it with higher price

    // calculation is done where day.open < day+n.close
    // or, for the final date where day.open < day.close
    private BigDecimal calculateMaxProfit(String code, Date from, Date to) {
        BigDecimal maxProfit = new BigDecimal(BigInteger.ZERO);
        List<Stock> stocksForDates = stockService.getStocksForPeriod(code, from, to);
        Stock[] stockForDatesArray = stocksForDates.toArray(Stock[]::new);

        for (int i = 0; i < stockForDatesArray.length; i++) {
            Stock stockBuy = stockForDatesArray[i];
            Stock stockSell = null;

            for (int j = i + 1; j < stockForDatesArray.length; j++) {
                if (stockBuy.getOpen().compareTo(stockForDatesArray[j].getClose()) < 0) {
                    stockSell = stockForDatesArray[j];
                    // additional logs to monitor logic
                    log.info("Stock buy date: {} sell stock date: {} stock buy price: {} stock sell price: {}", stockBuy.getDate(), stockSell.getDate(), stockBuy.getOpen(), stockSell.getClose());
                    break;
                }
            }

            if (stockSell == null) {
                stockSell = stockBuy;
            }

            // selling price (close) must exist and must be higher than buy (open) price
            if (stockBuy.getOpen().compareTo(stockSell.getClose()) < 0) { // buy < sell (open < close)
                maxProfit = maxProfit.add(stockSell.getClose().subtract(stockBuy.getOpen()));
            }
        }
        return maxProfit;
    }

    private StockDetailsForPeriod getStockDetailsForPeriod(String code, Date from, Date to) {
        log.info("Retrieving stock info for the period from: {} to: {}", from, to);

        // since not strictly defined
        // as the best buy the lowest LOW value is selected
        // as the best sell the highest HIGH value is selected

        // retrieve best buy
        Stock bestBuyStock = stockService.getBestBuy(code, from, to);
        BestBuyDto bestBuyDto = null;
        if (bestBuyStock != null) {
            bestBuyDto = BestBuyDto.builder()
                    .bestBuyDate(bestBuyStock.getDate())
                    .bestBuyClose(bestBuyStock.getClose())
                    .bestBuyLow(bestBuyStock.getLow())
                    .build();
        }

        // retrieve best sell
        Stock bestSellStock = stockService.getBestSell(code, from, to);
        BestSellDto bestSellDto = null;
        if (bestSellStock != null) {
            bestSellDto = BestSellDto.builder()
                    .bestSellDate(bestSellStock.getDate())
                    .bestSellClose(bestSellStock.getClose())
                    .bestSellHigh(bestSellStock.getHigh())
                    .build();
        }

        return StockDetailsForPeriod.builder()
                .bestBuy(bestBuyDto)
                .bestSell(bestSellDto)
                .build();
    }

    private Pair<Date, Date> getPeriodPair(Date fromDate, Date toDate, String period) {
        LocalDate fromPeriod = fromDate.toLocalDate();
        LocalDate toPeriod = toDate.toLocalDate();
        long periodSize = ChronoUnit.DAYS.between(fromPeriod, toPeriod);

        LocalDate requestedPeriodFrom;
        LocalDate requestedPeriodTo;
        if (PREVIOUS_PERIOD.equals(period)) {
            requestedPeriodFrom = fromPeriod.minusDays(periodSize + 1);
            requestedPeriodTo = fromPeriod.minusDays(1);
        } else {
            requestedPeriodFrom = toPeriod.plusDays(1);
            requestedPeriodTo = toPeriod.plusDays(periodSize + 1);
        }

        return Pair.of(Date.valueOf(requestedPeriodFrom), Date.valueOf(requestedPeriodTo));
    }
}
