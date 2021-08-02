package com.wherex.appventas.repository;

import com.wherex.appventas.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import com.wherex.appventas.entity.Detail;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DetailRepository extends JpaRepository<Detail, Long> {
    @Query(value="SELECT * FROM cliente c where c.nombre like %:name% and estado = 1 limit 3", nativeQuery = true)
    List<Client> findByName(String name);
}
