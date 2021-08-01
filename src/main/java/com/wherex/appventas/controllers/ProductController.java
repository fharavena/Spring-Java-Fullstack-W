package com.wherex.appventas.controllers;

import com.wherex.appventas.entity.Product;
import com.wherex.appventas.service.IService.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private IProductService productService;

    @GetMapping(value="")
    public List<Product> list(){
        return productService.findByCantidadGreaterThan(0.0);
    }
}
