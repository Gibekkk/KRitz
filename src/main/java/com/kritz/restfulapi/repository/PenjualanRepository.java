package com.kritz.restfulapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kritz.restfulapi.model.Penjualan;

public interface PenjualanRepository extends JpaRepository<Penjualan, String> {
}
