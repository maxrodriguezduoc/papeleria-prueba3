package producto.producto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import producto.producto.dto.CategoriaDTO;
import producto.producto.model.Categoria;
import producto.producto.repository.CategoriaRepository;

@Service
@Transactional
@Slf4j
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    public CategoriaDTO crear(Categoria categoria) {
        log.info("Intentando registrar una nueva categoría: {}", categoria.getNombre());

        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            log.error("Falla al crear: El nombre de la categoría está vacío");
            throw new RuntimeException("El nombre de la categoría es obligatorio y no puede estar vacío.");
        }
        
        Categoria guardada = categoriaRepository.save(categoria);
        log.info("Categoría registrada con éxito. ID asignado: {}", guardada.getIdCategoria());

        return convertirADTO(guardada);
    }

    public List<CategoriaDTO> obtenerTodas() {
        log.info("Consultando el listado de categorías activas");
        
        return categoriaRepository.findAll().stream()
                .filter(Categoria::isActivo)
                .map(this::convertirADTO)
                .toList();
    }

    public CategoriaDTO obtenerPorId(Integer id) {
        log.info("Buscando categoría con ID: {}", id);
        
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la categoría con ID: " + id));

        if (!categoria.isActivo()) {
            log.warn("La categoría ID {} existe pero está marcada como inactiva (borrado lógico)", id);
            throw new RuntimeException("La categoría solicitada ya no está disponible.");
        }

        return convertirADTO(categoria);
    }

    public CategoriaDTO actualizar(Integer id, Categoria categoria) {
        log.info("Iniciando actualización para la categoría ID: {}", id);
        
        Categoria existente = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar: Categoría no encontrada."));

        if (!existente.isActivo()) {
            throw new RuntimeException("No se puede modificar una categoría que está inactiva.");
        }

        if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nuevo nombre de la categoría no puede estar vacío.");
        }

        existente.setNombre(categoria.getNombre().trim());
        log.info("Categoría ID {} modificada con éxito", id);

        return convertirADTO(existente);
    }

    public void eliminar(Integer id) {
        log.warn("Procesando solicitud de baja (borrado lógico) para categoría ID: {}", id);
        
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede eliminar: El ID ingresado no existe."));

        if (!categoria.isActivo()) {
            log.info("La categoría ID {} ya se encontraba inactiva en el sistema", id);
            return;
        }

        categoria.setActivo(false);
        categoriaRepository.save(categoria);
        
        log.info("La categoría ID {} ha sido desactivada correctamente", id);
    }

    private CategoriaDTO convertirADTO(Categoria categoria) {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setIdCategoria(categoria.getIdCategoria());
        dto.setNombre(categoria.getNombre());
        dto.setActivo(categoria.isActivo());
        return dto;
    }
}
