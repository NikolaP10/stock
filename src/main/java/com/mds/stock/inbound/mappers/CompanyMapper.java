package com.mds.stock.inbound.mappers;

import com.mds.stock.inbound.dto.CompanyDto;
import com.mds.stock.outbound.dao.models.Company;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface CompanyMapper {

    CompanyDto fromEntityToDto(Company company);

    Company fromDtoToEntity(CompanyDto companyDto);
}
