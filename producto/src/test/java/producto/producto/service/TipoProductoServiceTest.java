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

import producto.producto.dto.TipoProductoDTO;
import producto.producto.model.TipoProducto;
import producto.producto.repository.TipoProductoRepository;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class TipoProductoServiceTest {

    @Mock
    private TipoProductoRepository tipoProductoRepository;
    
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient.Builder webClientBuilder; 

    @InjectMocks
    private TipoProductoService tipoProductoService; 

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearTipoProducto() {
        Integer idTipoFake = faker.number().numberBetween(1, 100);
        String nombreTest = faker.options().option("Cuaderno", "Lápiz", "Archivador", "Regla");

        TipoProducto tipoNuevo = new TipoProducto();
        tipoNuevo.setNombre(nombreTest);
        tipoNuevo.setActivo(true);

        when(tipoProductoRepository.save(any(TipoProducto.class))).thenAnswer(i -> {
            TipoProducto tp = i.getArgument(0);
            tp.setId_tipo_producto(idTipoFake);
            return tp;
        });

        TipoProductoDTO resultado = tipoProductoService.crear(tipoNuevo);

        assertNotNull(resultado);
        assertEquals(idTipoFake, resultado.getId_tipo_producto());
        assertEquals(nombreTest, resultado.getNombre());
        assertTrue(resultado.isActivo());
        verify(tipoProductoRepository, times(1)).save(any(TipoProducto.class));
    }

    @Test
    void testObtenerTodos() {
        TipoProducto tipoExistente = new TipoProducto();
        tipoExistente.setId_tipo_producto(faker.number().positive());
        tipoExistente.setNombre("Carpeta");
        tipoExistente.setActivo(true);

        when(tipoProductoRepository.findAll()).thenReturn(List.of(tipoExistente));

        List<TipoProductoDTO> resultados = tipoProductoService.obtenerActivos();

        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals("Carpeta", resultados.get(0).getNombre());
        verify(tipoProductoRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId() {
        Integer idBuscado = 6;
        TipoProducto tipoMock = new TipoProducto();
        tipoMock.setId_tipo_producto(idBuscado);
        tipoMock.setNombre("Tijeras");

        when(tipoProductoRepository.findById(idBuscado)).thenReturn(Optional.of(tipoMock));

        TipoProductoDTO resultado = tipoProductoService.buscarPorId(idBuscado);

        assertNotNull(resultado);
        assertEquals(idBuscado, resultado.getId_tipo_producto());
        assertEquals("Tijeras", resultado.getNombre());
        verify(tipoProductoRepository, times(1)).findById(idBuscado);
    }

    @Test
    void testEliminar() {
        Integer idAEliminar = 22;
        TipoProducto tipoActivo = new TipoProducto();
        tipoActivo.setId_tipo_producto(idAEliminar);
        tipoActivo.setActivo(true);

        when(tipoProductoRepository.findById(idAEliminar)).thenReturn(Optional.of(tipoActivo));

        tipoProductoService.eliminar(idAEliminar);

        // Verificamos el comportamiento de baja lógica (pasa a modo inactivo en false)
        assertFalse(tipoActivo.isActivo(), "El estado del tipo de producto debió cambiar a false (baja lógica)");
        verify(tipoProductoRepository, times(1)).save(tipoActivo);
    }

    @Test
    void testActualizarTipoProducto() {
        Integer idActualizar = 4;
        
        TipoProducto tipoEnBD = new TipoProducto();
        tipoEnBD.setId_tipo_producto(idActualizar);
        tipoEnBD.setNombre("Plumón Viejo");

        TipoProducto datosNuevos = new TipoProducto();
        datosNuevos.setNombre("Plumón Recargable");

        when(tipoProductoRepository.findById(idActualizar)).thenReturn(Optional.of(tipoEnBD));
        when(tipoProductoRepository.save(any(TipoProducto.class))).thenAnswer(i -> i.getArgument(0));

        TipoProductoDTO resultado = tipoProductoService.actualizar(idActualizar, datosNuevos);

        assertNotNull(resultado);
        assertEquals("Plumón Recargable", tipoEnBD.getNombre());
        assertEquals("Plumón Recargable", resultado.getNombre());
        verify(tipoProductoRepository, times(1)).save(tipoEnBD);
    }
}