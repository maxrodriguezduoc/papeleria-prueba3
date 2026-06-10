package com.cliente.cliente.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.cliente.cliente.model.Cliente;


public interface ClienteRepository extends JpaRepository <Cliente, Integer>  {

}
