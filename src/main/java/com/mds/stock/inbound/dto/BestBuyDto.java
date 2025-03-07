package com.mds.stock.inbound.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@Builder
public class BestBuyDto {

    private Date bestBuyDate;
    private BigDecimal bestBuyLow;
    private BigDecimal bestBuyClose;
}
