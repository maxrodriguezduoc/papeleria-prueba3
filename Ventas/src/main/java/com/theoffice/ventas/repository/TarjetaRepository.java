package com.theoffice.ventas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.theoffice.ventas.model.Tarjeta;

@Repository
public interface TarjetaRepository extends JpaRepository <Tarjeta, Integer>{

}
