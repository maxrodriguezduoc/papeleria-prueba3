package Ubicacion.Local.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ubicacion.Local.dto.ComunaDTO;
import Ubicacion.Local.model.Comuna;
import Ubicacion.Local.model.Region;
import Ubicacion.Local.repository.ComunaRepository;
import Ubicacion.Local.repository.RegionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ComunaService {

    @Autowired
    private ComunaRepository comunaRepository;

    @Autowired
    private RegionRepository regionRepository;

    public List<ComunaDTO> obtenerTodos() {
        log.info("Obteniendo lista de comunas");

        return comunaRepository.findAll().stream()
                .filter(Comuna::isActivo)
                .map(this::convertirADTO)
                .toList();
    }

    public ComunaDTO buscarPorId(Integer id) {
        log.info("Buscando comuna con ID: {}", id);

        Comuna comuna = comunaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comuna no encontrada!"));

        if (!comuna.isActivo()) {
            log.warn("Comuna inactiva!");
            throw new RuntimeException("Debe seleccionar una comuna activa!");
        }

        return convertirADTO(comuna);
    }

    public void eliminarComuna(Integer id) {
        log.info("Intentando eliminar comuna con ID: {}", id);

        Comuna comuna = comunaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error al eliminar comuna! Comuna no encontrada!"));

        if (!comuna.isActivo()) {
            log.info("Comuna inactiva");
            return;
        }

        comuna.setActivo(false);
        comunaRepository.save(comuna);

        log.info("Comuna eliminada exitosamente!");
    }

    public ComunaDTO guardarComuna(Comuna comuna) {
        log.info("Creando comuna: {}", comuna.getNombreComuna());

        if (comuna.getNombreComuna() == null || comuna.getNombreComuna().trim().isEmpty()) {
            throw new RuntimeException("Nombre de comuna obligatorio!");
        }

        Region region = regionRepository.findById(comuna.getRegion().getIdRegion())
                .orElseThrow(() -> new RuntimeException("Region no encontrada!"));

        if (!region.isActivo()) {
            throw new RuntimeException("Se debe seleccionar una region activa");
        }

        Comuna guardada = comunaRepository.save(comuna);

        log.info("Comuna registrada exitosamente!");

        return convertirADTO(guardada);
    }

    public ComunaDTO actualizarComuna(Integer id, Comuna comuna) {
        log.info("Actualizando comuna con ID: {}", id);

        Comuna existente = comunaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comuna no encontrada!"));

        if (!existente.isActivo()) {
            throw new RuntimeException("Se debe seleccionar una comun activa!");
        }

        if (comuna.getNombreComuna() == null || comuna.getNombreComuna().trim().isEmpty()) {
            throw new RuntimeException("Nombre de comuna obligatorio!");
        }

        existente.setNombreComuna(comuna.getNombreComuna().trim());
        existente.setCodigoPostal(comuna.getCodigoPostal());

        if (comuna.getRegion() != null) {
            Region region = regionRepository.findById(comuna.getRegion().getIdRegion())
                    .orElseThrow(() -> new RuntimeException("Region no encontrada!"));

            if (!region.isActivo()) {
                throw new RuntimeException("Se debe seleccionar una region activa!");
            }

            existente.setRegion(region);
        }

        log.info("Comuna actualizada exitosamente!");

        return convertirADTO(existente);
    }

    private ComunaDTO convertirADTO(Comuna comuna) {
        ComunaDTO dto = new ComunaDTO();
        dto.setIdComuna(comuna.getIdComuna());
        dto.setNombreComuna(comuna.getNombreComuna());
        dto.setCodigoPostal(comuna.getCodigoPostal());
        dto.setRegionId(comuna.getRegion().getIdRegion());
        dto.setActivo(comuna.isActivo());
        return dto;
    }
}