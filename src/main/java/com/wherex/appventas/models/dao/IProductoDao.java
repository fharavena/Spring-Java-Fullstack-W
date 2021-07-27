package com.wherex.appventas.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.wherex.appventas.models.entity.Producto;

public interface IProductoDao extends CrudRepository<Producto, Long> {
}
