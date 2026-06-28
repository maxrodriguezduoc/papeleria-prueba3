package Ubicacion.Local.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import Ubicacion.Local.model.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Integer>{
    boolean existsByNombreRegion(String nombreRegion);
}