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

import producto.producto.dto.MarcaDTO;
import producto.producto.model.Marca;
import producto.producto.repository.MarcaRepository;
import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class MarcaServiceTest {

    @Mock
    private MarcaRepository marcaRepository;
    
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient.Builder webClientBuilder; 

    @InjectMocks
    private MarcaService marcaService; 

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearMarca() {
        Integer idMarcaFake = faker.number().numberBetween(1, 100);
        String nombreTest = faker.commerce().brand(); // Faker genera nombres de marcas reales (Apple, Nike, etc.)

        Marca marcaNueva = new Marca();
        marcaNueva.setNombre_marca(nombreTest);
        marcaNueva.setActivo(true);

        when(marcaRepository.save(any(Marca.class))).thenAnswer(i -> {
            Marca m = i.getArgument(0);
            m.setId_marcas(idMarcaFake);
            return m;
        });

        MarcaDTO resultado = marcaService.crearMarca(marcaNueva);

        assertNotNull(resultado);
        assertEquals(idMarcaFake, resultado.getId_marcas());
        assertEquals(nombreTest, resultado.getNombre_marca());
        assertTrue(resultado.isActivo());
        verify(marcaRepository, times(1)).save(any(Marca.class));
    }

    @Test
    void testObtenerTodos() {
        Marca marcaExistente = new Marca();
        marcaExistente.setId_marcas(faker.number().positive());
        marcaExistente.setNombre_marca("Pilot");
        marcaExistente.setActivo(true);

        when(marcaRepository.findAll()).thenReturn(List.of(marcaExistente));

        List<MarcaDTO> resultados = marcaService.obtenerTodos();

        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals("Pilot", resultados.get(0).getNombre_marca());
        verify(marcaRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId() {
        Integer idBuscado = 4;
        Marca marcaMock = new Marca();
        marcaMock.setId_marcas(idBuscado);
        marcaMock.setNombre_marca("Proarte");

        when(marcaRepository.findById(idBuscado)).thenReturn(Optional.of(marcaMock));

        MarcaDTO resultado = marcaService.buscarPorId(idBuscado);

        assertNotNull(resultado);
        assertEquals(idBuscado, resultado.getId_marcas());
        assertEquals("Proarte", resultado.getNombre_marca());
        verify(marcaRepository, times(1)).findById(idBuscado);
    }

    @Test
    void testEliminar() {
        Integer idAEliminar = 9;
        Marca marcaActiva = new Marca();
        marcaActiva.setId_marcas(idAEliminar);
        marcaActiva.setActivo(true);

        when(marcaRepository.findById(idAEliminar)).thenReturn(Optional.of(marcaActiva));

        marcaService.eliminarMarca(idAEliminar);

        // Verificamos que pase a modo inactivo (false) en lugar de borrarse
        assertFalse(marcaActiva.isActivo(), "El estado de la marca debió cambiar a false (baja lógica)");
        verify(marcaRepository, times(1)).save(marcaActiva);
    }

    @Test
    void testActualizarMarca() {
        Integer idActualizar = 11;
        
        Marca marcaEnBD = new Marca();
        marcaEnBD.setId_marcas(idActualizar);
        marcaEnBD.setNombre_marca("Artel Antigua");

        Marca datosNuevos = new Marca();
        datosNuevos.setNombre_marca("Artel Chile");

        when(marcaRepository.findById(idActualizar)).thenReturn(Optional.of(marcaEnBD));
        when(marcaRepository.save(any(Marca.class))).thenAnswer(i -> i.getArgument(0));

        MarcaDTO resultado = marcaService.actualizarMarca(idActualizar, datosNuevos);

        assertNotNull(resultado);
        assertEquals("Artel Chile", marcaEnBD.getNombre_marca());
        assertEquals("Artel Chile", resultado.getNombre_marca());
        verify(marcaRepository, times(1)).save(marcaEnBD);
    }
}
