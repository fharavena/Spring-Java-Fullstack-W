package com.wherex.appventas.service.IService;

import com.wherex.appventas.entity.Product;

import java.util.List;

public interface IProductService {
    public List<Product> findByCantidadGreaterThan(Double zero);
}
