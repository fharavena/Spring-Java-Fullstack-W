package com.wherex.appventas.service;

import com.wherex.appventas.repository.ProductRepository;
import com.wherex.appventas.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;


    @Transactional(readOnly = true)
    public List<Product> findByCantidadGreaterThan(Double zero) {
        return (List<Product>) productRepository.findByCantidadGreaterThan(zero);
    }
}
