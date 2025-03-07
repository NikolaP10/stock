package com.mds.stock.inbound.mappers;

import com.mds.stock.inbound.dto.StockDto;
import com.mds.stock.outbound.dao.models.Stock;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface StockMapper {

    StockDto fromEntityToDto(Stock stock);

    Stock fromDtoToEntity(StockDto stockDto);

    List<StockDto> fromEntityListToDtoList(List<Stock> stockList);

}
