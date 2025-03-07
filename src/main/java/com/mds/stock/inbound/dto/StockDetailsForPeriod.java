package com.mds.stock.inbound.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockDetailsForPeriod {

    private BestBuyDto bestBuy;
    private BestSellDto bestSell;
}
