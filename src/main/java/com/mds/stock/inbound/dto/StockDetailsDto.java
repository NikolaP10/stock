package com.mds.stock.inbound.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StockDetailsDto {

    private StockDetailsAnalysisDto requestedPeriod;
    private StockDetailsForPeriod previousPeriod;
    private StockDetailsForPeriod followingPeriod;
}
