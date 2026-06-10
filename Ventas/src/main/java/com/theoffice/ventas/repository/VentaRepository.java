package com.theoffice.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.theoffice.ventas.model.Venta;

@Repository
public interface VentaRepository extends JpaRepository <Venta, Integer> {

}
