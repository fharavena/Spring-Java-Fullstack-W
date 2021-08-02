package com.wherex.appventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wherex.appventas.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    public List<Product> findByCantidadGreaterThan(Double estado);

    @Query(value="SELECT * FROM producto c where c.nombre like %:name% and cantidad > 0 limit 3", nativeQuery = true)
    List<Product> findByName(String name);
}
