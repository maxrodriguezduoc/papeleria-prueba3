package Ubicacion.Local.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Ubicacion.Local.dto.ColaboradorDTO;
import Ubicacion.Local.model.Cargo;
import Ubicacion.Local.model.Colaborador;
import Ubicacion.Local.model.Colaboradores;
import Ubicacion.Local.model.Local;
import Ubicacion.Local.repository.CargoRepository;
import Ubicacion.Local.repository.ColaboradorRepository;
import Ubicacion.Local.repository.LocalRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ColaboradorService {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private LocalRepository localRepository;

    public List<ColaboradorDTO> obtenerTodos() {
        log.info("Obteniendo lista de colaboradores!");

        return colaboradorRepository.findAll().stream()
            .filter(Colaborador::isActivo)
            .map(this::convertirADTO)
            .toList();
    }

    public ColaboradorDTO buscarPorId(Integer id) {
        log.info("Buscando colaborador con ID: {}", id);

        Colaborador colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colaborador no encontrado!"));

        if (!colaborador.isActivo()) {
            log.warn("Colaborador inactivo!");
            throw new RuntimeException("El Colaborador no se encuentra disponible!");
        }

        return convertirADTO(colaborador);
    }

    public void eliminarColaborador(Integer id) {
        log.warn("Intentando eliminar colaborador con ID: {}", id);

        Colaborador colaborador = colaboradorRepository.findById(id)
                .orElseThrow(() -> 
                new RuntimeException("Error al eliminar! El ID ingresado no existe!"));

        if (!colaborador.isActivo()) {
            log.info("El colaborador seleccionado se encuentra inactivo!");
            return;
        }

        colaborador.setActivo(false);
        colaboradorRepository.save(colaborador);

        log.info("El colaborador se ha desactivado exitosamente!");
    }

    public ColaboradorDTO guardarColaborador(Colaborador colaborador) {
        log.info("Creando colaborador: {}", colaborador.getNombreColaborador());

        if (colaborador.getNombreColaborador() == null || colaborador.getNombreColaborador().trim().isEmpty()) {
            log.error("Nombre del colaborador vacio!");
            throw new RuntimeException("Nombre de colaborador es obligatorio!");
        }

        Cargo cargo = cargoRepository.findById(colaborador.getCargo().getIdCargo())
                .orElseThrow(() -> new RuntimeException("Cargo no encontrado!"));

        if (!cargo.isActivo()) {
            throw new RuntimeException("Se debe asignar a un cargo activo!");
        }

        Local local = localRepository.findById(colaborador.getLocales().get(0).getLocal().getIdLocal())
                .orElseThrow(() -> new RuntimeException("Local no encontrado!"));

        if (!local.isActivo()) {
            throw new RuntimeException("Se debe asignar a un local activo!");
        }

        Colaboradores puente = new Colaboradores();
        puente.setColaborador(colaborador); // Enlace hacia el colaborador (Padre)
        puente.setLocal(local);             // Enlace hacia la sucursal física
        puente.setActivo(true);

        // Añadimos el registro puente a la lista del colaborador principal
        colaborador.getLocales().add(puente);

        Colaborador guardado = colaboradorRepository.save(colaborador);

        log.info("Colaborador guardado exitosamente!");

        return convertirADTO(guardado);
    }

    public ColaboradorDTO actualizarColaborador(Integer id, Colaborador colaborador) {
        log.info("Actualizando colaborador con ID: {}", id);

        Colaborador existente = colaboradorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colaborador no encontrado!"));

        if (!existente.isActivo()) {
            throw new RuntimeException("Solo se puede actualizar colaboradores activo!");
        }

        if (colaborador.getNombreColaborador() == null || colaborador.getNombreColaborador().trim().isEmpty()) {
            throw new RuntimeException("Nombre de colaborador obligatorio!");
        }

        existente.setNombreColaborador(colaborador.getNombreColaborador().trim());

        if (colaborador.getCargo() != null) {
            Cargo cargo = cargoRepository.findById(colaborador.getCargo().getIdCargo())
                    .orElseThrow(() -> new RuntimeException("Cargo no encontrado!"));

            if (!cargo.isActivo()) {
                throw new RuntimeException("Se debe asignar a un cargo activo!");
            }

            existente.setCargo(cargo);
        }

        if (colaborador.getLocales()!= null) {
            Local local = localRepository.findById(colaborador.getLocales().get(0).getLocal().getIdLocal())
                    .orElseThrow(() -> new RuntimeException("Local no encontrado!"));

            if (!local.isActivo()) {
                throw new RuntimeException("Se debe asignar a un local activo!");
            }

            existente.getLocales().clear();

            Colaboradores nuevoPuente = new Colaboradores();
            nuevoPuente.setColaborador(existente);
            nuevoPuente.setLocal(local);
            nuevoPuente.setActivo(true);

            existente.getLocales().add(nuevoPuente);
        }

        log.info("Colaborador actualizado exitosamente!");

        return convertirADTO(existente);
    }

    private ColaboradorDTO convertirADTO(Colaborador colaborador) {
    ColaboradorDTO dto = new ColaboradorDTO();
    dto.setIdColaborador(colaborador.getIdColaborador());
    dto.setNombreColaborador(colaborador.getNombreColaborador());
    dto.setActivo(colaborador.isActivo());

    if (colaborador.getCargo() != null) {
        dto.setCargoId(colaborador.getCargo().getIdCargo());
    }

    if (colaborador.getLocales() != null && !colaborador.getLocales().isEmpty()) {
        Integer idLocal = colaborador.getLocales().get(0).getLocal().getIdLocal();
        dto.setLocalId(idLocal);
    }

    return dto;
    }
}