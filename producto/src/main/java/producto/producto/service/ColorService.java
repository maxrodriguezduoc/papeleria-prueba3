package producto.producto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import producto.producto.dto.ColorDTO;
import producto.producto.model.Color;
import producto.producto.repository.ColorRepository;

@Slf4j
@Service
@Transactional
public class ColorService {
    @Autowired
    private ColorRepository coloresRepository;

    public ColorDTO crear(Color color) {
        log.info("Intentando registrar un nuevo color: {}", color.getNombre_color());
        if (color.getNombre_color() == null || color.getNombre_color().trim().isEmpty()) {
            log.error("Falla al crear: El nombre del color está vacío");
            throw new RuntimeException("El nombre del color es obligatorio.");
        }
        color.setNombre_color(color.getNombre_color().trim());
        coloresRepository.save(color);
        log.info("Color registrado con éxito. ID: {}", color.getId_color());
        return convertirADTO(color);
    }

    public List<ColorDTO> obtenerTodos() {
        log.info("Consultando el listado de colores activos");
        return coloresRepository.findAll().stream()
             .filter(Color::isActivo) 
             .map(this::convertirADTO)
             .toList();
    }


    public ColorDTO buscarColorPorId(Integer id){
        log.info("Buscando color con ID: {}", id);

        Color color = coloresRepository.findById(id)
            .orElseThrow(() -> {
                log.error("No se encontró el color con ID: {}", id);
                    return new RuntimeException("Color no encontrado");
            });
        return convertirADTO(color);
    }

    public void eliminarColor(Integer id) {
        log.warn("Procesando baja lógica para el color ID: {}", id);
        Color color = coloresRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("No se puede eliminar: El ID {} no existe.", id);
                    return new RuntimeException("No se puede eliminar: El ID ingresado no existe.");
                });

        if (!color.isActivo()) {
            log.info("El color ID {} ya se encontraba inactivo", id);
            return;
        }
        color.setActivo(false);
        coloresRepository.save(color);
        
        log.info("El color ID {} ha sido desactivado correctamente", id);
    }

    public ColorDTO actualizarColor(Integer id,Color colores){
        log.info("Intentando actualizar color con ID: {}", id);
        Color color = coloresRepository.findById(id)
            .orElseThrow(() -> {
                log.error("Falla al actualizar: El color con ID {} no existe", id);
                return new RuntimeException("¡El color no existe en los registros!");
            });
        if (colores.getNombre_color() != null && !colores.getNombre_color().trim().isEmpty()) {
            color.setNombre_color(colores.getNombre_color().trim());
        }
        log.info("Color con ID: {} actualizado con éxito en la base de datos", id);
        return convertirADTO(color);
    }


    private ColorDTO convertirADTO(Color color){
        ColorDTO dto = new ColorDTO();
        dto.setId_color(color.getId_color());
        dto.setNombre_color(color.getNombre_color());
        return dto;
    }
}
