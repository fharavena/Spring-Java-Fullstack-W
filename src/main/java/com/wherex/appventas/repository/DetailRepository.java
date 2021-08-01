package com.wherex.appventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wherex.appventas.entity.Detail;

public interface DetailRepository extends JpaRepository<Detail, Long> {
}
