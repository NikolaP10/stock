package com.mds.stock.inbound.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
public class StockDetailsAnalysisDto {

    private StockDetailsForPeriod trade;
    private BigDecimal closeDiff;
    private BigDecimal maxProfit;
    private Map<String, BigDecimal> othersMaxProfit;
}
