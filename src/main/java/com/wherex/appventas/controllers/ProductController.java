package com.wherex.appventas.controllers;

import com.wherex.appventas.entity.Product;
import com.wherex.appventas.repository.ProductRepository;
import com.wherex.appventas.service.IService.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private IProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping(value = "")
    public ResponseEntity<?> list() {

        Map<String, Object> response = new HashMap<>();

        response.put("data", productService.findByCantidadGreaterThan(0.0));
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        Map<String, Object> response = new HashMap<>();

        Product product  = productRepository.getById(id);


        response.put("data", product);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PostMapping(value = "")
    public ResponseEntity<?> create(@RequestBody(required = true) Product product) {
        Map<String, Object> response = new HashMap<>();

        if (product.getId() != null) {
            response.put("error","viene el id en el body");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        response.put("data", productRepository.save(product));
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "")
    public ResponseEntity<?> update(@RequestBody(required = true) Product product) {
        Map<String, Object> response = new HashMap<>();

        if (product.getId() == null) {
            response.put("error","no viene el id en el body");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        response.put("data", productRepository.save(product));
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        Map<String, Object> response = new HashMap<>();
        productRepository.delete(productRepository.getById(id));
        response.put("data", "Success");
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }



}
