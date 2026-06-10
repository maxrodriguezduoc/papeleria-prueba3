package Ubicacion.Local.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Ubicacion.Local.model.Local;

@Repository
public interface LocalRepository extends JpaRepository<Local, Integer>{
}