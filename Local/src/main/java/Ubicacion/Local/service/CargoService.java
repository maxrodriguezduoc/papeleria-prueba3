package Ubicacion.Local.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ubicacion.Local.dto.CargoDTO;
import Ubicacion.Local.model.Cargo;
import Ubicacion.Local.repository.CargoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class CargoService {

    @Autowired
    private CargoRepository cargoRepository;

    public List<CargoDTO> obtenerTodos() {
        log.info("Obteniendo lista de todos los cargos disponibles!");

        return cargoRepository.findAll().stream()
            .filter(Cargo::isActivo)
            .map(this::convertirADTO)
            .toList();
    }

    public CargoDTO buscarPorId(Integer id) {
        log.info("Buscando cargo con ID: {}", id);

        Cargo cargo = cargoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cargo no encontrado!"));

        if (!cargo.isActivo()) {
            log.warn("Cargo inactivo!");
            throw new RuntimeException("El Cargo no se encuentra disponible!");
        }

        return convertirADTO(cargo);
    }

    public void eliminarCargo(Integer id) {
        log.warn("Intentando eliminar cargo con ID: {}", id);

        Cargo cargo = cargoRepository.findById(id)
                .orElseThrow(() -> 
                new RuntimeException("Error al eliminar! El ID ingresado no existe!"));

        if (!cargo.isActivo()) {
            log.info("El cargo seleccionado se encuentra inactivo!");
            return;
        }

        cargo.setActivo(false);
        cargoRepository.save(cargo);

        log.info("El cargo se ha desactivado exitosamente!");
    }

    public CargoDTO guardarCargo(Cargo cargo) {
        log.info("Guardando nuevo cargo: {}", cargo.getNombreCargo());

        if (cargo.getNombreCargo() == null || cargo.getNombreCargo().trim().isEmpty()) {
            log.error("Nombre de Cargo no puede estar vacio!");
            throw new RuntimeException("Nombre de Cargo obligatorio!");
        }

        Cargo guardado = cargoRepository.save(cargo);

        return convertirADTO(guardado);
    }

    public CargoDTO actualizarCargo(Integer id, Cargo cargo) {
        log.info("Actualizando cargo!");

        Cargo cargoExistente = cargoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Error al actualizar! Cargo no encontrado!"));

        if (!cargoExistente.isActivo()) {
            throw new RuntimeException("Error al actualizar! Cargo inactivo!");
        }

        if (cargo.getNombreCargo() == null || cargo.getNombreCargo().trim().isEmpty()) {
            throw new RuntimeException("Nombre de cargo es obligatorio!");
            
        }

        cargoExistente.setNombreCargo(cargo.getNombreCargo().trim());

        Cargo actualizado = cargoRepository.save(cargoExistente);

        log.info("Cargo actualizado con exito!");

        return convertirADTO(actualizado);
    }

    private CargoDTO convertirADTO(Cargo cargo) {
        CargoDTO dto = new CargoDTO();
        dto.setIdCargo(cargo.getIdCargo());
        dto.setNombreCargo(cargo.getNombreCargo());
        return dto;
    }
}