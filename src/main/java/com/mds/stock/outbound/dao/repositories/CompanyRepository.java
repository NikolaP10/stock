package com.mds.stock.outbound.dao.repositories;

import com.mds.stock.outbound.dao.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CompanyRepository extends JpaRepository<Company, Integer>, JpaSpecificationExecutor<Company> {

    boolean existsByNameAndCode(String name, String code);

    boolean existsById(Integer id);
}
