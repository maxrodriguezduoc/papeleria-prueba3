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
import producto.producto.dto.ColorDTO;
import producto.producto.model.Color;
import producto.producto.repository.ColorRepository;
import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class ColorServiceTest {

    @Mock
    private ColorRepository colorRepository;

    @InjectMocks
    private ColorService colorService; 
    
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient.Builder webClientBuilder; 

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearColor() {
        Integer idColorFake = faker.number().numberBetween(1, 100);
        String nombreTest = faker.color().name(); // Faker genera nombres de colores (red, blue, etc.)

        Color colorNuevo = new Color();
        colorNuevo.setNombre_color(nombreTest);
        colorNuevo.setActivo(true);

        when(colorRepository.save(any(Color.class))).thenAnswer(i -> {
            Color c = i.getArgument(0);
            c.setId_color(idColorFake);
            return c;
        });

        ColorDTO resultado = colorService.crear(colorNuevo);

        assertNotNull(resultado);
        assertEquals(idColorFake, resultado.getId_color());
        assertEquals(nombreTest, resultado.getNombre_color());
        assertTrue(resultado.isActivo());
        verify(colorRepository, times(1)).save(any(Color.class));
    }

    @Test
    void testObtenerTodos() {
        Color colorExistente = new Color();
        colorExistente.setId_color(faker.number().positive());
        colorExistente.setNombre_color("Azul");
        colorExistente.setActivo(true);

        when(colorRepository.findAll()).thenReturn(List.of(colorExistente));

        List<ColorDTO> resultados = colorService.obtenerTodos();

        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals("Azul", resultados.get(0).getNombre_color());
        verify(colorRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId() {
        Integer idBuscado = 8;
        Color colorMock = new Color();
        colorMock.setId_color(idBuscado);
        colorMock.setNombre_color("Rojo");

        when(colorRepository.findById(idBuscado)).thenReturn(Optional.of(colorMock));

        ColorDTO resultado = colorService.buscarColorPorId(idBuscado);

        assertNotNull(resultado);
        assertEquals(idBuscado, resultado.getId_color());
        assertEquals("Rojo", resultado.getNombre_color());
        verify(colorRepository, times(1)).findById(idBuscado);
    }

    @Test
    void testEliminar() {
        Integer idAEliminar = 14;
        Color colorActivo = new Color();
        colorActivo.setId_color(idAEliminar);
        colorActivo.setActivo(true);

        when(colorRepository.findById(idAEliminar)).thenReturn(Optional.of(colorActivo));

        colorService.eliminarColor(idAEliminar);

        assertFalse(colorActivo.isActivo(), "El estado del color debió cambiar a false (baja lógica)");
        verify(colorRepository, times(1)).save(colorActivo);
    }

    @Test
    void testActualizarColor() {
        Integer idActualizar = 3;
        
        Color colorEnBD = new Color();
        colorEnBD.setId_color(idActualizar);
        colorEnBD.setNombre_color("Verde Claro");

        Color datosNuevos = new Color();
        datosNuevos.setNombre_color("Verde Oscuro");

        when(colorRepository.findById(idActualizar)).thenReturn(Optional.of(colorEnBD));
        when(colorRepository.save(any(Color.class))).thenAnswer(i -> i.getArgument(0));

        ColorDTO resultado = colorService.actualizarColor(idActualizar, datosNuevos);

        assertNotNull(resultado);
        assertEquals("Verde Oscuro", colorEnBD.getNombre_color());
        assertEquals("Verde Oscuro", resultado.getNombre_color());
        verify(colorRepository, times(1)).save(colorEnBD);
    }
}
