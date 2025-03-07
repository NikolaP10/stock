package com.mds.stock.inbound.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {

    private Integer id;
    private String name;
    private String code;
    private Date founded;
    private String country;
    private String city;
    private String address;
    private String owner;
}
