package com.theoffice.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.theoffice.ventas.model.Transferencia;

@Repository
public interface TransferenciaRepository extends JpaRepository <Transferencia, Integer>{

}
