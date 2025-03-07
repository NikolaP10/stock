package com.mds.stock.inbound.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@Builder
public class BestSellDto {

    private Date bestSellDate;
    private BigDecimal bestSellHigh;
    private BigDecimal bestSellClose;
}
