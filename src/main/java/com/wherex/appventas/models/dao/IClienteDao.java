package com.wherex.appventas.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.wherex.appventas.models.entity.Cliente;

public interface IClienteDao extends CrudRepository<Cliente, Long>{
    
}
