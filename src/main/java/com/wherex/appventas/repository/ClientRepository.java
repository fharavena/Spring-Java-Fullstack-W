package com.wherex.appventas.repository;

import com.wherex.appventas.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wherex.appventas.entity.Client;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    public List<Client> findByEstado(Integer estado);

    @Query(value="SELECT * FROM cliente c where c.nombre like %:name% and estado = 1 limit 3", nativeQuery = true)
    List<Client> findByName(String name);
}
