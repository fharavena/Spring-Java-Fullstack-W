package com.wherex.appventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wherex.appventas.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    public List<Product> findByCantidadGreaterThan(Double estado);
}
