package com.alumni.portal.repository;

import com.alumni.portal.entity.Product;
import com.alumni.portal.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByProductType(ProductType productType);
    Optional<Product> findByName(String name);
}
