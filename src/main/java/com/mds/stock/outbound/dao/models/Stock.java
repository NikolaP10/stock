package com.mds.stock.outbound.dao.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;


@Data
@Entity
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Integer id;

    @Column(name = "code", length = 5, nullable = false)
    String code;

    @Column(name = "open")
    BigDecimal open;

    @Column(name = "high")
    BigDecimal high;

    @Column(name = "low")
    BigDecimal low;

    @Column(name = "close")
    BigDecimal close;

    @Column(name = "adj_close")
    BigDecimal adjClose;

    @Column(name = "volume")
    BigDecimal volume;

    @Column(name = "date", nullable = false)
    Date date;
}
