package Ubicacion.Local.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ubicacion.Local.dto.RegionDTO;
import Ubicacion.Local.model.Region;
import Ubicacion.Local.repository.RegionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    public List<RegionDTO> obtenerTodos() {
        log.info("Obteniendo lista de regiones");

        return regionRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    public void eliminarRegion(Integer id) {
        log.info("Intentando eliminar región con ID: {}", id);

        Region region = regionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Error al buscar region! Region no encontrada!", id);
                    return new RuntimeException("Región no encontrada!");
                });

        region.setActivo(false);
        regionRepository.save(region);

        log.info("Region eliminada exitosamente!!");
    }

    public RegionDTO buscarPorId(Integer id) {
        log.info("Buscando región con ID: {}", id);

        Region region = regionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Error al buscar! Region no encontrada!");
                    return new RuntimeException("Regio no encontrada!");
                });

        return convertirADTO(region);
    }

    public RegionDTO guardarRegion(Region region) {
        log.info("Creando región: {}", region.getNombreRegion());

        if (region.getNombreRegion() == null || region.getNombreRegion().trim().isEmpty()) {
            throw new RuntimeException("Nombre de region obligatorio!");
        }

        Region regi = new Region();
        regi.setNombreRegion(region.getNombreRegion().trim());
        regi.setActivo(true);

        Region guardada = regionRepository.save(regi);

        log.info("Region guardada exitosamente!");

        return convertirADTO(guardada);
    }

    public RegionDTO actualizarRegion(Integer id, Region region) {
        log.info("Actualizando región con ID: {}", id);

        Region existente = regionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Error al buscar region! Region no encontrada!");
                    return new RuntimeException("Región no encontrada!");
                });

        if (region.getNombreRegion() != null) {
            existente.setNombreRegion(region.getNombreRegion().trim());
        }

        log.info("Región actualizada exitosamente!");

        return convertirADTO(existente);
    }

    private RegionDTO convertirADTO(Region region) {
        RegionDTO dto = new RegionDTO();
        dto.setIdRegion(region.getIdRegion());
        dto.setNombreRegion(region.getNombreRegion());
        dto.setActivo(region.isActivo());
        return dto;
    }
}