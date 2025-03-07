package com.mds.stock.outbound.dao.models;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@Data
@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Integer id;

    @Column(name = "name", length = 100, nullable = false)
    String name;

    @Column(name = "code", length = 5, nullable = false)
    String code;

    @Column(name = "founded", nullable = false)
    Date founded;

    @Column(name = "country", length = 100)
    String country;

    @Column(name = "city", length = 100)
    String city;

    @Column(name = "address", length = 200)
    String address;

    @Column(name = "owner", length = 200)
    String owner;
}
