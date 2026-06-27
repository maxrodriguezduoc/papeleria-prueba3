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

import producto.producto.dto.CategoriaDTO;
import producto.producto.model.Categoria;
import producto.producto.repository.CategoriaRepository;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService; 
    
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient.Builder webClientBuilder; 
    
    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearCategoria() {
        Integer idCatFake = faker.number().numberBetween(1, 50);
        String nombreTest = faker.options().option("Papelería", "Escritorio", "Computación", "Arte");

        Categoria categoriaNueva = new Categoria();
        categoriaNueva.setNombre(nombreTest);
        categoriaNueva.setActivo(true);

        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(i -> {
            Categoria c = i.getArgument(0);
            c.setIdCategoria(idCatFake); // Asigna el ID usando CamelCase según tu DTO
            return c;
        });

        CategoriaDTO resultado = categoriaService.crear(categoriaNueva);

        assertNotNull(resultado);
        assertEquals(idCatFake, resultado.getIdCategoria());
        assertEquals(nombreTest, resultado.getNombre());
        assertTrue(resultado.isActivo());
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }

    @Test
    void testObtenerTodos() {
        Categoria categoriaExistente = new Categoria();
        categoriaExistente.setIdCategoria(faker.number().positive());
        categoriaExistente.setNombre("Oficina");
        categoriaExistente.setActivo(true);

        when(categoriaRepository.findAll()).thenReturn(List.of(categoriaExistente));

        List<CategoriaDTO> resultados = categoriaService.obtenerTodas();

        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals("Oficina", resultados.get(0).getNombre());
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId() {
        Integer idBuscado = 3;
        Categoria categoriaMock = new Categoria();
        categoriaMock.setIdCategoria(idBuscado);
        categoriaMock.setNombre("Escolar");

        when(categoriaRepository.findById(idBuscado)).thenReturn(Optional.of(categoriaMock));

        CategoriaDTO resultado = categoriaService.obtenerPorId(idBuscado);

        assertNotNull(resultado);
        assertEquals(idBuscado, resultado.getIdCategoria());
        assertEquals("Escolar", resultado.getNombre());
        verify(categoriaRepository, times(1)).findById(idBuscado);
    }

    @Test
    void testEliminar() {
        Integer idAEliminar = 12;
        Categoria categoriaActiva = new Categoria();
        categoriaActiva.setIdCategoria(idAEliminar);
        categoriaActiva.setActivo(true);

        when(categoriaRepository.findById(idAEliminar)).thenReturn(Optional.of(categoriaActiva));

        categoriaService.eliminar(idAEliminar);

        assertFalse(categoriaActiva.isActivo(), "El estado de la categoría debió cambiar a false (baja lógica)");
        verify(categoriaRepository, times(1)).save(categoriaActiva);
    }

    @Test
    void testActualizarCategoria() {
        Integer idActualizar = 5;
        
        Categoria categoriaEnBD = new Categoria();
        categoriaEnBD.setIdCategoria(idActualizar);
        categoriaEnBD.setNombre("Tecno Antigua");

        Categoria datosNuevos = new Categoria();
        datosNuevos.setNombre("Tecnología y Accesorios");

        when(categoriaRepository.findById(idActualizar)).thenReturn(Optional.of(categoriaEnBD));
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(i -> i.getArgument(0));

        CategoriaDTO resultado = categoriaService.actualizar(idActualizar, categoriaEnBD);
        assertNotNull(resultado);
        assertEquals("Tecnología y Accesorios", categoriaEnBD.getNombre());
        assertEquals("Tecnología y Accesorios", resultado.getNombre());
        verify(categoriaRepository, times(1)).save(categoriaEnBD);
    }
}