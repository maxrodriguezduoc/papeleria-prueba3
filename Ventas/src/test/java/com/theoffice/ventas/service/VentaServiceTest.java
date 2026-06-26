package com.theoffice.ventas.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.theoffice.ventas.DTO.ProductoExternoDTO;
import com.theoffice.ventas.DTO.VentaDTO;
import com.theoffice.ventas.model.Pago;
import com.theoffice.ventas.model.Venta;
import com.theoffice.ventas.repository.VentaRepository;

import net.datafaker.Faker;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock
    private VentaRepository ventaRepository;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient.Builder webClientBuilder; 

    @InjectMocks
    private VentaService ventaService; 

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrear() {
        Integer idProducto = faker.number().numberBetween(1, 100);
        int cantidad = 2;
        int precioUnitario = 1500;

        ProductoExternoDTO producto = new ProductoExternoDTO();
        producto.setId_producto(idProducto);
        producto.setNombre_producto(faker.commerce().productName());
        producto.setPrecio_producto(precioUnitario);
        producto.setStock(10);

        Pago pago = new Pago();
        pago.setMonto(cantidad * precioUnitario);
        
        Venta ventaNueva = new Venta();
        ventaNueva.setCantidad(cantidad);
        ventaNueva.setPagos(new ArrayList<>(List.of(pago)));

        when(webClientBuilder.build().get().uri(anyString(), anyInt()).retrieve().bodyToMono(ProductoExternoDTO.class))
                .thenReturn(Mono.just(producto));
        when(webClientBuilder.build().put().uri(anyString(), anyInt()).bodyValue(any()).retrieve().bodyToMono(Void.class))
                .thenReturn(Mono.empty());
        when(ventaRepository.save(any(Venta.class))).thenAnswer(i -> {
            Venta v = i.getArgument(0);
            v.setId_venta(1);
            return v;
        });

        VentaDTO resultado = ventaService.crear(ventaNueva, idProducto);

        assertNotNull(resultado);
        assertEquals(3000, resultado.getTotal_venta());
        assertEquals(8, producto.getStock());
        verify(ventaRepository, times(1)).save(any(Venta.class));
    }

    @Test
    void testObtenerTodos() {
        Venta ventaExistente = new Venta();
        ventaExistente.setId_venta(faker.number().positive());
        ventaExistente.setCantidad(1);
        ventaExistente.setId_producto(10);
        
        ProductoExternoDTO productoMock = new ProductoExternoDTO();
        productoMock.setNombre_producto("Lápiz Pasta");

        when(ventaRepository.findAll()).thenReturn(List.of(ventaExistente));
        when(webClientBuilder.build().get().uri(anyString(), anyInt()).retrieve().bodyToMono(ProductoExternoDTO.class))
                .thenReturn(Mono.just(productoMock));

        List<VentaDTO> resultados = ventaService.obtenerTodos();

        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals("Lápiz Pasta", resultados.get(0).getNombreProducto());
        verify(ventaRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId() {
        Integer idBuscado = 5;
        Venta ventaMock = new Venta();
        ventaMock.setId_venta(idBuscado);
        ventaMock.setId_producto(20);
        
        ProductoExternoDTO productoMock = new ProductoExternoDTO();
        productoMock.setNombre_producto("Cuaderno Universitario");

        when(ventaRepository.findById(idBuscado)).thenReturn(Optional.of(ventaMock));
        when(webClientBuilder.build().get().uri(anyString(), anyInt()).retrieve().bodyToMono(ProductoExternoDTO.class))
                .thenReturn(Mono.just(productoMock));

        VentaDTO resultado = ventaService.buscarPorId(idBuscado);

        assertNotNull(resultado);
        assertEquals(idBuscado, resultado.getId_venta());
        assertEquals("Cuaderno Universitario", resultado.getNombreProducto());
        verify(ventaRepository, times(1)).findById(idBuscado);
    }

    @Test
    void testEliminar() {
        Integer idAEliminar = 10;
        Venta ventaActiva = new Venta();
        ventaActiva.setId_venta(idAEliminar);
        ventaActiva.setActivo(true);

        when(ventaRepository.findById(idAEliminar)).thenReturn(Optional.of(ventaActiva));

        ventaService.eliminar(idAEliminar);

        assertFalse(ventaActiva.isActivo(), "El estado de la venta debió cambiar a false (baja lógica)");
        verify(ventaRepository, times(1)).save(ventaActiva);
    }

    @Test
    void testActualizarVentas() {
        Integer idActualizar = 7;
        
        Venta ventaEnBD = new Venta();
        ventaEnBD.setId_venta(idActualizar);
        ventaEnBD.setCantidad(2);
        ventaEnBD.setTotal_venta(5000);
        ventaEnBD.setId_producto(30);

        Venta datosNuevos = new Venta();
        datosNuevos.setCantidad(5);
        datosNuevos.setTotal_venta(12500);

        ProductoExternoDTO productoMock = new ProductoExternoDTO();
        productoMock.setNombre_producto("Destacador");

        when(ventaRepository.findById(idActualizar)).thenReturn(Optional.of(ventaEnBD));
        when(webClientBuilder.build().get().uri(anyString(), anyInt()).retrieve().bodyToMono(ProductoExternoDTO.class))
                .thenReturn(Mono.just(productoMock));

        VentaDTO resultado = ventaService.actualizarVentas(idActualizar, datosNuevos);

        assertNotNull(resultado);
        assertEquals(5, ventaEnBD.getCantidad(), "La cantidad en la entidad debió actualizarse a 5");
        assertEquals(12500, ventaEnBD.getTotal_venta(), "El total en la entidad debió actualizarse a 12500");

        assertEquals(5, resultado.getCantidad());
        assertEquals(12500, resultado.getTotal_venta());
        assertEquals("Destacador", resultado.getNombreProducto());
    }
}