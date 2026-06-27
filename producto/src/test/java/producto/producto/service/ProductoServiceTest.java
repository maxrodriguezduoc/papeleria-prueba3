package producto.producto.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

import producto.producto.dto.ProductoDTO;
import producto.producto.model.Producto;
import producto.producto.repository.ProductoRepository;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService; 
    
    
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient.Builder webClientBuilder; 
    
    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearProducto() {
        Integer idProducto = faker.number().numberBetween(1, 100);
        String nombreTest = faker.commerce().productName();
        int precioTest = faker.number().numberBetween(1000, 10000);
        int stockTest = 50;

        Producto productoNuevo = new Producto();
        productoNuevo.setNombre_producto(nombreTest);
        productoNuevo.setPrecio_producto(precioTest);
        productoNuevo.setStock(stockTest);

        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> {
            Producto p = i.getArgument(0);
            p.setId_producto(idProducto);
            return p;
        });

       
        ProductoDTO resultado = productoService.crearProducto(productoNuevo);

        assertNotNull(resultado);
        assertEquals(idProducto, resultado.getId_producto());
        assertEquals(nombreTest, resultado.getNombre_producto());
        assertEquals(precioTest, resultado.getPrecio_producto());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    @Test
    void testObtenerTodos() {
        Producto productoExistente = new Producto();
        productoExistente.setId_producto(faker.number().positive());
        productoExistente.setNombre_producto("Lápiz Pasta");
        productoExistente.setPrecio_producto(1500);
        productoExistente.setStock(10);
        productoExistente.setActivo(true);

        when(productoRepository.findAll()).thenReturn(List.of(productoExistente));

        List<ProductoDTO> resultados = productoService.obtenerTodos();

        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals("Lápiz Pasta", resultados.get(0).getNombre_producto());
        verify(productoRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId() {
        Integer idBuscado = 5;
        Producto productoMock = new Producto();
        productoMock.setId_producto(idBuscado);
        productoMock.setNombre_producto("Cuaderno Universitario");
        productoMock.setPrecio_producto(2500);

        when(productoRepository.findById(idBuscado)).thenReturn(Optional.of(productoMock));

        ProductoDTO resultado = productoService.buscarPorId(idBuscado);

        assertNotNull(resultado);
        assertEquals(idBuscado, resultado.getId_producto());
        assertEquals("Cuaderno Universitario", resultado.getNombre_producto());
        verify(productoRepository, times(1)).findById(idBuscado);
    }

    @Test
    void testEliminar() {
        Integer idAEliminar = 10;
        Producto productoActivo = new Producto();
        productoActivo.setId_producto(idAEliminar);
        productoActivo.setActivo(true);

        when(productoRepository.findById(idAEliminar)).thenReturn(Optional.of(productoActivo));

        productoService.eliminar(idAEliminar);

        assertFalse(productoActivo.isActivo(), "El estado del producto debió cambiar a false (baja lógica)");
        verify(productoRepository, times(1)).save(productoActivo);
    }

    @Test
    void testActualizarProducto() {
        Integer idActualizar = 7;
        
        Producto productoEnBD = new Producto();
        productoEnBD.setId_producto(idActualizar);
        productoEnBD.setNombre_producto("Destacador");
        productoEnBD.setPrecio_producto(5000);
        productoEnBD.setStock(2);

        Producto datosNuevos = new Producto();
        datosNuevos.setNombre_producto("Destacador Premium");
        datosNuevos.setPrecio_producto(12500);
        datosNuevos.setStock(5);

        when(productoRepository.findById(idActualizar)).thenReturn(Optional.of(productoEnBD));
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> i.getArgument(0));

        ProductoDTO resultado = productoService.actualizarProducto(idActualizar, datosNuevos);

        assertNotNull(resultado);
        assertEquals("Destacador Premium", productoEnBD.getNombre_producto());
        assertEquals(12500, productoEnBD.getPrecio_producto());

        // Verificamos que el DTO de salida exponga las propiedades actualizadas de forma idéntica
        assertEquals("Destacador Premium", resultado.getNombre_producto());
        assertEquals(12500, resultado.getPrecio_producto());
        verify(productoRepository, times(1)).save(productoEnBD);
    }
}