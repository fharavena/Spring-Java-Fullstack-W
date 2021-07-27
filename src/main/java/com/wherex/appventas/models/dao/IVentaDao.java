package com.wherex.appventas.models.dao;

import com.wherex.appventas.models.entity.Venta;
import org.springframework.data.repository.CrudRepository;

public interface IVentaDao extends CrudRepository<Venta, Long> {
}
