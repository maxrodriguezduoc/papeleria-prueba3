package test.java.com.theoffice.ventas.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.theoffice.ventas.DTO.TarjetaDTO;
import com.theoffice.ventas.model.Tarjeta;
import com.theoffice.ventas.repository.TarjetaRepository;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class TarjetaServiceTest {

    @Mock
    private TarjetaRepository tarjetaRepository;

    @InjectMocks
    private TarjetaService tarjetaService;

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private String generarFechaExpiracionValida() {
        Calendar cal = Calendar.getInstance();
        int anioFuturo = (cal.get(Calendar.YEAR) % 100) + 5; // Le sumamos 5 años al actual
        return "12/" + anioFuturo; // Siempre expirará en diciembre de un año futuro (ej: 12/31)
    }

    @Test
    void testCrear() {
        String numeroGenerado = faker.number().digits(16);
        String ultimosCuatro = numeroGenerado.substring(12);
        
        Tarjeta tarjetaEntrante = new Tarjeta();
        tarjetaEntrante.setNumeroTarjeta(numeroGenerado);
        tarjetaEntrante.setCvv(faker.number().digits(3));
        tarjetaEntrante.setFechaExpiracion(generarFechaExpiracionValida());
        tarjetaEntrante.setNombreBanco("   Banco de Chile   ");
        tarjetaEntrante.setNombreTitular(faker.name().fullName());

        when(tarjetaRepository.save(any(Tarjeta.class))).thenAnswer(invocation -> {
            Tarjeta t = invocation.getArgument(0);
            t.setIdTarjeta(42);
            return t;
        });

        TarjetaDTO resultado = tarjetaService.crear(tarjetaEntrante);

        assertNotNull(resultado);
        assertEquals(42, resultado.getIdTarjeta());
        assertEquals("Banco de Chile", resultado.getNombreBanco(), "El servicio debió aplicar trim() al nombre del banco");
        assertTrue(resultado.getUltimosCuatro().contains(ultimosCuatro), "El DTO debió enmascarar el número");
        assertTrue(resultado.isActivo(), "La tarjeta debió inicializarse como activa");
        
        verify(tarjetaRepository, times(1)).save(any(Tarjeta.class));
    }

    @Test
    void testObtenerTodas() {
        Tarjeta tarjetaActiva = new Tarjeta();
        tarjetaActiva.setIdTarjeta(1);
        tarjetaActiva.setNumeroTarjeta(faker.number().digits(16));
        tarjetaActiva.setActivo(true);

        Tarjeta tarjetaInactiva = new Tarjeta();
        tarjetaInactiva.setIdTarjeta(2);
        tarjetaInactiva.setActivo(false);

        when(tarjetaRepository.findAll()).thenReturn(List.of(tarjetaActiva, tarjetaInactiva));

        List<TarjetaDTO> resultados = tarjetaService.obtenerTodas();

        assertNotNull(resultados);
        assertEquals(1, resultados.size(), "Solo debió retornar la tarjeta activa");
        assertEquals(1, resultados.get(0).getIdTarjeta());
        
        verify(tarjetaRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId() {
        Integer idBuscado = 5;
        String numeroTarjeta = "1234567812345678";
        
        Tarjeta tarjetaExistente = new Tarjeta();
        tarjetaExistente.setIdTarjeta(idBuscado);
        tarjetaExistente.setNumeroTarjeta(numeroTarjeta);
        tarjetaExistente.setNombreBanco("Santander");
        tarjetaExistente.setActivo(true);

        when(tarjetaRepository.findById(idBuscado)).thenReturn(Optional.of(tarjetaExistente));

        TarjetaDTO resultado = tarjetaService.obtenerPorId(idBuscado);

        assertNotNull(resultado);
        assertEquals(idBuscado, resultado.getIdTarjeta());
        assertEquals("Santander", resultado.getNombreBanco());
        assertEquals("****-****-****-5678", resultado.getUltimosCuatro(), "El número debió ser enmascarado por el helper privado");
        
        verify(tarjetaRepository, times(1)).findById(idBuscado);
    }

    @Test
    void testActualizar() {
        Integer idActualizar = 7;
        
        Tarjeta tarjetaEnBD = new Tarjeta();
        tarjetaEnBD.setIdTarjeta(idActualizar);
        tarjetaEnBD.setNumeroTarjeta("1111222233334444");
        tarjetaEnBD.setNombreBanco("Banco Falabella");
        tarjetaEnBD.setActivo(true);

        String nuevoNumeroValido = faker.number().digits(16);
        Tarjeta datosNuevos = new Tarjeta();
        datosNuevos.setNumeroTarjeta(nuevoNumeroValido);
        datosNuevos.setNombreBanco(" Banco de Chile ");
        datosNuevos.setCvv("999");
        datosNuevos.setNombreTitular("Maximiliano");

        when(tarjetaRepository.findById(idActualizar)).thenReturn(Optional.of(tarjetaEnBD));

        TarjetaDTO resultado = tarjetaService.actualizar(idActualizar, datosNuevos);

        assertNotNull(resultado);
        assertEquals(nuevoNumeroValido, tarjetaEnBD.getNumeroTarjeta(), "El número debió actualizarse tras pasar el Regex");
        assertEquals("Banco de Chile", tarjetaEnBD.getNombreBanco(), "El nuevo banco debió recibir trim()");
        assertEquals("Maximiliano", tarjetaEnBD.getNombreTitular());
        assertEquals("999", tarjetaEnBD.getCvv());
        
        verify(tarjetaRepository, times(1)).findById(idActualizar);
    }

    @Test
    void testEliminar() {
        Integer idAEliminar = 10;
        
        Tarjeta tarjetaActiva = new Tarjeta();
        tarjetaActiva.setIdTarjeta(idAEliminar);
        tarjetaActiva.setActivo(true);

        when(tarjetaRepository.findById(idAEliminar)).thenReturn(Optional.of(tarjetaActiva));
        when(tarjetaRepository.save(any(Tarjeta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        tarjetaService.eliminar(idAEliminar);

        assertFalse(tarjetaActiva.isActivo(), "La propiedad 'activo' debió cambiar a false");
        verify(tarjetaRepository, times(1)).save(tarjetaActiva);
    }
}