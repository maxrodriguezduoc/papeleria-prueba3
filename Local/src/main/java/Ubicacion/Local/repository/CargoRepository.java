package Ubicacion.Local.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Ubicacion.Local.model.Cargo;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Integer>{
}