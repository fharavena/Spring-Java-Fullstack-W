package com.wherex.appventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wherex.appventas.entity.Client;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    public List<Client> findByEstado(Integer estado);

}
