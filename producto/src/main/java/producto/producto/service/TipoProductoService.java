package producto.producto.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import producto.producto.dto.TipoProductoDTO;
import producto.producto.model.TipoProducto;
import producto.producto.repository.TipoProductoRepository;

@Slf4j
@Service
@Transactional

public class TipoProductoService {
    @Autowired
    private TipoProductoRepository  tipoProductosRepository;

    public TipoProductoDTO crear(TipoProducto tipoProducto) {
        log.info("Creando nuevo tipo de producto: {}", tipoProducto.getNombre());
        tipoProducto.setActivo(true);
        TipoProducto guardado = tipoProductosRepository.save(tipoProducto);
        return convertirADTO(guardado);
    }

    public TipoProductoDTO buscarPorId(Integer id) {
        log.info("Buscando tipo de producto con ID: {}", id);
        TipoProducto tipo = tipoProductosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de producto no encontrado con ID: " + id));
        return convertirADTO(tipo);
    }
    
   
    public List<TipoProductoDTO> obtenerActivos() {
        log.info("Listando tipos de productos activos");
        return tipoProductosRepository.findAll().stream()
                .filter(TipoProducto::isActivo)
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public TipoProductoDTO actualizar(Integer id, TipoProducto tipoDetalles) {
        log.info("Actualizando tipo de producto ID: {}", id);
        TipoProducto tipo = tipoProductosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de producto no encontrado"));
        
        tipo.setNombre(tipoDetalles.getNombre());
        
        return convertirADTO(tipo);
    }


    public void eliminar(Integer id) {
        log.warn("Dando de baja lógica al tipo de producto ID: {}", id);
        TipoProducto tipo = tipoProductosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de producto no encontrado"));
        
        tipo.setActivo(false);
        tipoProductosRepository.save(tipo);
    }

    private TipoProductoDTO convertirADTO(TipoProducto tipo) {
        TipoProductoDTO dto = new TipoProductoDTO();
        dto.setId_tipo_producto(tipo.getId_tipo_producto());
        dto.setNombre(tipo.getNombre());
        dto.setActivo(tipo.isActivo());
        return dto;
    }
}
