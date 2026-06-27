package com.theoffice.ventas.service;

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

import com.theoffice.ventas.DTO.TransferenciaDTO;
import com.theoffice.ventas.model.Transferencia;
import com.theoffice.ventas.repository.TransferenciaRepository;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class TransferenciaServiceTest {

    @Mock
    private TransferenciaRepository transferenciaRepository;

    @InjectMocks
    private TransferenciaService transferenciaService;

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrear() {
        Transferencia nuevaTransferencia = new Transferencia();
        nuevaTransferencia.setBancoOrigen("Banco Santander");
        nuevaTransferencia.setNumeroCuentaOrigen(faker.number().digits(12));
        
        nuevaTransferencia.setBancoDestino("Banco de Chile");
        nuevaTransferencia.setNumeroCuentaDestino(faker.number().digits(12));

        nuevaTransferencia.setMonto(faker.number().numberBetween(10000, 1500000));

        when(transferenciaRepository.save(any(Transferencia.class))).thenAnswer(invocation -> {
            Transferencia t = invocation.getArgument(0);
            t.setIdTransferencia(999);
            return t;
        });

        TransferenciaDTO resultado = transferenciaService.crear(nuevaTransferencia);

        assertNotNull(resultado);
        assertEquals(999, resultado.getIdTransferencia());
        assertTrue(resultado.isActivo(), "Toda nueva transferencia debe iniciar en estado activo");
        
        verify(transferenciaRepository, times(1)).save(any(Transferencia.class));
    }

    @Test
    void testObtenerTodas() {
        Transferencia transActiva = new Transferencia();
        transActiva.setIdTransferencia(1);
        transActiva.setMonto(50000);
        transActiva.setActivo(true);

        Transferencia transInactiva = new Transferencia();
        transInactiva.setIdTransferencia(2);
        transInactiva.setActivo(false);

        when(transferenciaRepository.findAll()).thenReturn(List.of(transActiva, transInactiva));

        List<TransferenciaDTO> resultados = transferenciaService.obtenerTodas();

        assertNotNull(resultados);
        assertEquals(1, resultados.size(), "El filtro ignoró correctamente el registro inactivo");
        assertEquals(1, resultados.get(0).getIdTransferencia());
        
        verify(transferenciaRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId() {
        Integer idBuscado = 5;
        
        Transferencia transExistente = new Transferencia();
        transExistente.setIdTransferencia(idBuscado);
        transExistente.setBancoOrigen("BCI");
        transExistente.setMonto(25000);
        transExistente.setActivo(true);

        when(transferenciaRepository.findById(idBuscado)).thenReturn(Optional.of(transExistente));

        TransferenciaDTO resultado = transferenciaService.obtenerPorId(idBuscado);

        assertNotNull(resultado);
        assertEquals(idBuscado, resultado.getIdTransferencia());
        assertEquals("BCI", resultado.getBancoOrigen());
        assertEquals(25000, resultado.getMonto());
        
        verify(transferenciaRepository, times(1)).findById(idBuscado);
    }

    @Test
    void testActualizar() {
        Integer idActualizar = 7;
        
        Transferencia entidadBD = new Transferencia();
        entidadBD.setIdTransferencia(idActualizar);
        entidadBD.setBancoOrigen("Banco Estado");
        entidadBD.setActivo(true);

        Transferencia nuevosDatos = new Transferencia();
        nuevosDatos.setBancoOrigen("Scotiabank");
        nuevosDatos.setNumeroCuentaOrigen(faker.number().digits(10));
        nuevosDatos.setBancoDestino("Itaú");
        nuevosDatos.setNumeroCuentaDestino(faker.number().digits(10));
        nuevosDatos.setMonto(150000);

        when(transferenciaRepository.findById(idActualizar)).thenReturn(Optional.of(entidadBD));

        TransferenciaDTO resultado = transferenciaService.actualizar(idActualizar, nuevosDatos);

        assertNotNull(resultado);
        assertEquals("Scotiabank", entidadBD.getBancoOrigen(), "La propiedad original fue sobreescrita exitosamente");
        assertEquals(150000, entidadBD.getMonto());
        
        assertEquals("Scotiabank", resultado.getBancoOrigen());
        
        verify(transferenciaRepository, times(1)).findById(idActualizar);
    }

    @Test
    void testEliminar() {
        Integer idAEliminar = 10;
        
        Transferencia transActiva = new Transferencia();
        transActiva.setIdTransferencia(idAEliminar);
        transActiva.setActivo(true);

        when(transferenciaRepository.findById(idAEliminar)).thenReturn(Optional.of(transActiva));

        transferenciaService.eliminar(idAEliminar);

        assertFalse(transActiva.isActivo(), "El flag activo cambió a false");
        verify(transferenciaRepository, times(1)).save(transActiva); 
    }
}