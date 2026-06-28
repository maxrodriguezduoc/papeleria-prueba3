package Ubicacion.Local.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Ubicacion.Local.model.Comuna;

@Repository
public interface ComunaRepository extends JpaRepository<Comuna, Integer>{
    boolean existsByNombreComunaAndRegion_IdRegion(String nombreComuna, Integer idRegion);
}