package com.theoffice.ventas.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.theoffice.ventas.DTO.ProductoDTO;
import com.theoffice.ventas.DTO.VentaDTO;
import com.theoffice.ventas.model.Pago;
import com.theoffice.ventas.model.Venta;
import com.theoffice.ventas.repository.VentaRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class VentaService {

    @Autowired
    private VentaRepository ventaRepository;

    private WebClient.Builder webClientBuilder;

    public VentaService(VentaRepository ventaRepository, WebClient.Builder webClientBuilder) {
        this.ventaRepository = ventaRepository;
        this.webClientBuilder = webClientBuilder;
    }

    private ProductoDTO obtenerProductoRemoto(Integer idProducto) {
        log.info("Consultando producto ID {} vía WebClient a ms-productos", idProducto);
        return webClientBuilder.build()
                .get()
                .uri("http://ms-productos/api/v1/productos/{id}", idProducto)
                .retrieve()
                .bodyToMono(ProductoDTO.class)
                .block();
    }

    private void actualizarProductoRemoto(Integer idProducto, ProductoDTO productoActualizado) {
        log.info("Enviando actualización de stock para producto ID {} vía WebClient", idProducto);
        webClientBuilder.build()
                .put()
                .uri("http://ms-productos/api/v1/productos/{id}", idProducto) // Endpoint PUT de actualización del catálogo
                .bodyValue(productoActualizado) // Enviamos el DTO con el nuevo stock en el cuerpo de la petición
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public VentaDTO crear(Venta venta, Integer idProducto) {
        log.info("Iniciando creación de venta para Producto ID: {}", idProducto);
        
        // A. Buscamos el producto primero (Igual que como buscas en otros servicios)
        ProductoDTO producto = obtenerProductoRemoto(idProducto);

        // B. Validaciones de Regla de Negocio
        if (producto.getStock() < venta.getCantidad()) {
            log.error("Error de stock: Pedido {}, Disponible {}", venta.getCantidad(), producto.getStock());
            throw new RuntimeException("No hay suficiente stock para realizar esta venta.");
        }

        if (venta.getPagos() == null || venta.getPagos().isEmpty()) {
            log.error("Falla al crear venta: No se registraron métodos de pago");
            throw new RuntimeException("Debe ingresar al menos un método de pago para procesar la venta.");
        }
        
        int totalCalculado = producto.getPrecio_producto() * venta.getCantidad();
        venta.setTotal_venta(totalCalculado);

        // 1. Validar que la suma de los pagos coincida exactamente con el total de la venta
        int sumaPagos = venta.getPagos().stream()
                .mapToInt(Pago::getMonto)
                .sum();

        if (sumaPagos != venta.getTotal_venta()) {
            log.error("La suma de los pagos (${}) no coincide con el total de la venta (${})", sumaPagos, venta.getTotal_venta());
            throw new RuntimeException("El monto total de los pagos ingresados debe ser exactamente igual al total de la venta.");
        }

        // 2. Asociar físicamente cada pago a la venta
        for (Pago pago : venta.getPagos()) {
            pago.setVenta(venta);
            pago.setActivo(true);
        }

        venta.setActivo(true);

        producto.setStock(producto.getStock() - venta.getCantidad());
        
        actualizarProductoRemoto(idProducto, producto);

        Venta ventaGuardada = ventaRepository.save(venta);
        
        log.info("Venta creada exitosamente con ID: {}", ventaGuardada.getId_venta());
        
        return convertirADTO(ventaGuardada);
    }

    public List<VentaDTO> obtenerTodos() {
        log.info("Consultando el listado de ventas");
        return ventaRepository.findAll().stream()
                 .map(this::convertirADTO)
                 .toList();
    }

    public VentaDTO buscarPorId(Integer id) {
        log.info("Buscando venta con el ID: {}",id);
        Venta ventas = ventaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("¡Venta no encontrada!"));
        return convertirADTO(ventas);
    }

    public void eliminar(Integer id) {
        log.info("Esta eliminando la venta con el ID: {}",id);
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Id de venta no encontrada"));
        venta.setActivo(false);
        ventaRepository.save(venta);
    }

    public VentaDTO actualizarVentas(Integer id,Venta ventas){
        log.info("Actualizando venta con ID: {}", id);
        Venta venta = ventaRepository.findById(id).orElseThrow(() -> new RuntimeException("¡La venta no existe en los registros!"));
        if(ventas.getCantidad() != null){
            venta.setCantidad(ventas.getCantidad());
        }
        if(ventas.getTotal_venta() != null){
            venta.setTotal_venta(ventas.getTotal_venta());
        }
        return convertirADTO(venta);
    }

    private VentaDTO convertirADTO(Venta venta) {
        VentaDTO dto = new VentaDTO();
        dto.setId_venta(venta.getId_venta());
        dto.setCantidad(venta.getCantidad());
        dto.setTotal_venta(venta.getTotal_venta());
        dto.setFecha_venta(venta.getFecha_venta());
        dto.setActivo(venta.isActivo());
        
        String nombreProd = "Producto no disponible";
        try {
            ProductoDTO p = obtenerProductoRemoto(venta.getId_producto());
            if (p != null) {
                nombreProd = p.getNombre_producto();
            }
        } catch (Exception e) {
            log.error("Error al recuperar nombre para el DTO: {}", e.getMessage());
        }
    
        dto.setNombreProducto(nombreProd);
        return dto;
    }
}
