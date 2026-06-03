package producto.producto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import producto.producto.dto.ProductoDTO;
import producto.producto.model.Producto;
import producto.producto.repository.ProductoRepository;

@Slf4j
@Service
@Transactional
public class ProductoService {
    @Autowired
    private ProductoRepository productosRepository;

    public ProductoDTO crearProducto(Producto producto){
        log.info("Iniciando registro de nuevo producto: {}", producto.getNombre_producto());
        producto.setActivo(true);
        Producto guardado = productosRepository.save(producto);
        log.info("Producto registrado exitosamente. ID; {}, Stock inicial: {}" ,guardado.getId_producto(), guardado.getStock()); 
        return convertirADTO(guardado);
    }

    public List<ProductoDTO> obtenerTodos(){
        log.info("Cosultando listado de productos activos");
        return productosRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public ProductoDTO buscarPorId(Integer id) {
        Producto productos = productosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("¡Producto no encontrado!"));
        return convertirADTO(productos);
    }

    public void eliminar(Integer id) {
        log.warn("Solicitud para eliminar producto ID: {}", id);
        Producto producto = productosRepository.findById(id).orElseThrow(() ->{
            log.error("Falla al eliminar: El ID {} no existe", id);
                    return new RuntimeException("No se encontró el producto para eliminar.");
                });
        if (!producto.isActivo()) {
            log.info("El producto con el ID: {} ya se encuetra eliminado", id);
            return;
            
        }
        if (producto.getStock() > 0) {
            log.error("No se puede eliminar el productos ID : {} porque aun tiene {} unidades en stock", id, producto.getStock());
            throw new RuntimeException("No se puede eliminar un producto con existencias en bodega.");  
        }
        producto.setActivo(false);
        productosRepository.save(producto);
        log.info("Producto ID: {} ('{}') ha sido desactivado con éxito", id, producto.getNombre_producto());
    }

    public ProductoDTO actualizarProducto(Integer id, Producto datosNuevos) {
        
        log.info("Actualizando información del producto ID: {}", id);
        
        Producto producto = productosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("¡El producto no existe!"));


        producto.setNombre_producto(datosNuevos.getNombre_producto());
        producto.setPrecio_producto(datosNuevos.getPrecio_producto());
        producto.setStock(datosNuevos.getStock());
        if (producto.getStock() < 5) {
            log.warn("¡ALERTA! El producto '{}' (ID: {}) tiene stock bajo: {}", id ,producto.getNombre_producto(), id ,producto.getStock());
        }
        return convertirADTO(producto);
    }
    
    private ProductoDTO convertirADTO(Producto producto) {
        ProductoDTO dto = new ProductoDTO();
        dto.setId_producto(producto.getId_producto());
        dto.setNombre_producto(producto.getNombre_producto());
        dto.setPrecio_producto(producto.getPrecio_producto());
        dto.setStock(producto.getStock()); 
        dto.setActivo(producto.isActivo()); 
        
        return dto;
    }
}
