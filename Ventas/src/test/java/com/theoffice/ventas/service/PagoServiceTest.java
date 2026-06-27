package test.java.com.theoffice.ventas.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.theoffice.ventas.DTO.PagoDTO;
import com.theoffice.ventas.model.Pago;
import com.theoffice.ventas.model.Venta;
import com.theoffice.ventas.repository.PagoRepository;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private PagoService pagoService;

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrear() {
        Integer montoSimulado = faker.number().numberBetween(1000, 50000);
        
        Pago pagoEntrante = new Pago();
        pagoEntrante.setFormaPago("   tarjeta_credito   "); 
        pagoEntrante.setMonto(montoSimulado);

        when(pagoRepository.save(any(Pago.class))).thenAnswer(invocation -> {
            Pago p = invocation.getArgument(0);
            p.setIdPago(99); 
            return p;
        });

        PagoDTO resultado = pagoService.crear(pagoEntrante);

        assertNotNull(resultado);
        assertEquals(99, resultado.getIdPago());
        assertEquals("TARJETA_CREDITO", resultado.getFormaPago(), "El servicio debió limpiar los espacios y convertir a mayúsculas");
        assertEquals(montoSimulado, resultado.getMonto());
        assertTrue(resultado.isActivo(), "El pago debió inicializarse como activo");
        
        verify(pagoRepository, times(1)).save(any(Pago.class));
    }

    @Test
    void testObtenerTodos() {
        Pago pagoActivo = new Pago();
        pagoActivo.setIdPago(1);
        pagoActivo.setFormaPago("EFECTIVO");
        pagoActivo.setMonto(5000);
        pagoActivo.setActivo(true);

        Pago pagoInactivo = new Pago();
        pagoInactivo.setIdPago(2);
        pagoInactivo.setActivo(false);

        when(pagoRepository.findAll()).thenReturn(List.of(pagoActivo, pagoInactivo));

        List<PagoDTO> resultados = pagoService.obtenerTodos();

        assertNotNull(resultados);
        assertEquals(1, resultados.size(), "El filtro stream debió omitir el pago inactivo");
        assertEquals(1, resultados.get(0).getIdPago());
        
        verify(pagoRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId() {

        Integer idBuscado = 5;
        
        Venta ventaVinculada = new Venta();
        ventaVinculada.setId_venta(101);

        Pago pagoExistente = new Pago();
        pagoExistente.setIdPago(idBuscado);
        pagoExistente.setFormaPago("TRANSFERENCIA");
        pagoExistente.setMonto(15000);
        pagoExistente.setActivo(true);
        pagoExistente.setVenta(ventaVinculada);

        when(pagoRepository.findById(idBuscado)).thenReturn(Optional.of(pagoExistente));

        PagoDTO resultado = pagoService.obtenerPorId(idBuscado);

        assertNotNull(resultado);
        assertEquals(idBuscado, resultado.getIdPago());
        assertEquals("TRANSFERENCIA", resultado.getFormaPago());
        assertEquals(101, resultado.getIdVenta(), "El DTO debió extraer correctamente el ID de la Venta asociada");
        
        verify(pagoRepository, times(1)).findById(idBuscado);
    }

    @Test
    void testActualizar() {
        Integer idActualizar = 7;
        
        Pago pagoEnBD = new Pago();
        pagoEnBD.setIdPago(idActualizar);
        pagoEnBD.setFormaPago("EFECTIVO");
        pagoEnBD.setMonto(1000);
        pagoEnBD.setActivo(true);

        Pago datosNuevos = new Pago();
        datosNuevos.setFormaPago("tarjeta_debito");
        datosNuevos.setMonto(2500);

        when(pagoRepository.findById(idActualizar)).thenReturn(Optional.of(pagoEnBD));

        PagoDTO resultado = pagoService.actualizar(idActualizar, datosNuevos);

        assertNotNull(resultado);
        assertEquals(2500, pagoEnBD.getMonto(), "El monto en la entidad debió sobreescribirse");
        assertEquals("TARJETA_DEBITO", pagoEnBD.getFormaPago(), "El nuevo texto debió normalizarse a mayúsculas");
        
        assertEquals(2500, resultado.getMonto());
        assertEquals("TARJETA_DEBITO", resultado.getFormaPago());
        
        verify(pagoRepository, times(1)).findById(idActualizar);
    }

    @Test
    void testEliminar() {
        Integer idAEliminar = 10;
        
        Pago pagoActivo = new Pago();
        pagoActivo.setIdPago(idAEliminar);
        pagoActivo.setActivo(true);

        when(pagoRepository.findById(idAEliminar)).thenReturn(Optional.of(pagoActivo));
        
        when(pagoRepository.save(any(Pago.class))).thenAnswer(invocation -> invocation.getArgument(0));

        pagoService.eliminar(idAEliminar);
        
        assertFalse(pagoActivo.isActivo(), "La propiedad 'activo' debió cambiar a false tras la anulación lógica");
        verify(pagoRepository, times(1)).save(pagoActivo);
    }
}