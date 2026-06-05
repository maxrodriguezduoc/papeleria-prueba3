package com.theoffice.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.theoffice.ventas.model.TipoPago;

@Repository
public interface TipoPagoRepository extends JpaRepository <TipoPago, Integer>{

}
