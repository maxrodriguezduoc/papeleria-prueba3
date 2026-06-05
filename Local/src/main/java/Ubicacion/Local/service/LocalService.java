package Ubicacion.Local.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ubicacion.Local.dto.LocalDTO;
import Ubicacion.Local.model.Local;
import Ubicacion.Local.repository.LocalRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class LocalService {

    @Autowired
    private LocalRepository localRepository;

    public List<LocalDTO> obtenerTodos() {
        log.info("Obteniendo lista de locales");

        return localRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .toList();
    }

    public LocalDTO buscarPorId(Integer id) {
        log.info("Buscando local con ID: {}", id);

        Local local = localRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Error al buscar Local! Local no encontrado!");
                    return new RuntimeException("Local no encontrado!");
                });

        return convertirADTO(local);
    }

    public void eliminarLocal(Integer id) {
        log.info("Intentando eliminar local con ID: {}", id);

        Local local = localRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Local no encontrado!");
                    return new RuntimeException("Local no encontrado!");
                });

        local.setActivo(false);
        localRepository.save(local);

        log.info("Local eliminado correctamente!");
    }

    public LocalDTO guardarLocal(Local local) {
        log.info("Creando local: {}", local.getNombreLocal());

        if (local.getNombreLocal() == null || local.getNombreLocal().trim().isEmpty()) {
            throw new RuntimeException("Nombre de local obligatorio!");
        }

        if (local.getDireccion() == null || local.getDireccion().trim().isEmpty()) {
            throw new RuntimeException("Direccion de local obligatorio!");
        }

        Local guardado = localRepository.save(local);

        log.info("Local guardado exitosamente!");

        return convertirADTO(guardado);
    }

    public LocalDTO actualizarLocal(Integer id, Local local) {
        log.info("Actualizando local con ID: {}", id);

        Local existente = localRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Error al actualizar! Local no encontrado!");
                    return new RuntimeException("Local no encontrado!");
                });

        if (local.getNombreLocal() != null) {
            existente.setNombreLocal(local.getNombreLocal().trim());
        }

        if (local.getDireccion() != null) {
            existente.setDireccion(local.getDireccion().trim());
        }

        if (local.getComuna() != null) {
            existente.setComuna(local.getComuna());
        }
        log.info("Local actualizado exitosamente!");
        Local actualizado = localRepository.save(existente);
        return convertirADTO(actualizado);
    }

    private LocalDTO convertirADTO(Local local) {
        LocalDTO dto = new LocalDTO();
        dto.setIdLocal(local.getIdLocal());
        dto.setNombreLocal(local.getNombreLocal());
        dto.setDireccion(local.getDireccion());
        dto.setActivo(local.isActivo());

        if (local.getComuna() != null) {
            dto.setComunaId(local.getComuna().getIdComuna());
        }
        return dto;
    }
}