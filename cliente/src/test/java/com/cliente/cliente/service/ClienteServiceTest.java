package com.cliente.cliente.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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

import com.cliente.cliente.dto.ClienteDTO;
import com.cliente.cliente.model.Cliente;
import com.cliente.cliente.repository.ClienteRepository;

import net.datafaker.Faker;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService; 

    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearCliente() {
        Integer idClienteFake = faker.number().numberBetween(1, 500);
        String rutFake = faker.idNumber().valid(); // Genera un identificador único simulado
        String nombreFake = faker.name().fullName(); // Genera un nombre y apellido real

        Cliente clienteNuevo = new Cliente();
        clienteNuevo.setRut(rutFake);
        clienteNuevo.setNombreCompleto(nombreFake);
        clienteNuevo.setActivo(true);

        when(clienteRepository.save(any(Cliente.class))).thenAnswer(i -> {
            Cliente c = i.getArgument(0);
            c.setIdCliente(idClienteFake);
            return c;
        });

        ClienteDTO resultado = clienteService.crear(clienteNuevo);

        assertNotNull(resultado);
        assertEquals(idClienteFake, resultado.getIdCliente());
        assertEquals(rutFake, resultado.getRut());
        assertEquals(nombreFake, resultado.getNombreCompleto());
        assertTrue(resultado.isActivo());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testObtenerTodos() {
        Cliente clienteExistente = new Cliente();
        clienteExistente.setIdCliente(faker.number().positive());
        clienteExistente.setRut("12345678-9");
        clienteExistente.setNombreCompleto("Michael Scott");
        clienteExistente.setActivo(true);

        when(clienteRepository.findAll()).thenReturn(List.of(clienteExistente));

        List<ClienteDTO> resultados = clienteService.obtenerTodos();

        assertNotNull(resultados);
        assertEquals(1, resultados.size());
        assertEquals("Michael Scott", resultados.get(0).getNombreCompleto());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId() {
        Integer idBuscado = 45;
        Cliente clienteMock = new Cliente();
        clienteMock.setIdCliente(idBuscado);
        clienteMock.setRut("98765432-1");
        clienteMock.setNombreCompleto("Dwight Schrute");

        when(clienteRepository.findById(idBuscado)).thenReturn(Optional.of(clienteMock));

        ClienteDTO resultado = clienteService.obtenerPorId(idBuscado);

        assertNotNull(resultado);
        assertEquals(idBuscado, resultado.getIdCliente());
        assertEquals("Dwight Schrute", resultado.getNombreCompleto());
        verify(clienteRepository, times(1)).findById(idBuscado);
    }

    @Test
    void testEliminar() {
        Integer idAEliminar = 101;
        Cliente clienteActivo = new Cliente();
        clienteActivo.setIdCliente(idAEliminar);
        clienteActivo.setActivo(true);

        when(clienteRepository.findById(idAEliminar)).thenReturn(Optional.of(clienteActivo));

        clienteService.eliminar(idAEliminar);

        // Verificamos el comportamiento de baja lógica: cambia a modo inactivo (false) en vez de destruirse
        assertFalse(clienteActivo.isActivo(), "El estado del cliente debió cambiar a false (baja lógica)");
        verify(clienteRepository, times(1)).save(clienteActivo);
    }

    @Test
    void testActualizarCliente() {
        Integer idActualizar = 22;
        
        Cliente clienteEnBD = new Cliente();
        clienteEnBD.setIdCliente(idActualizar);
        clienteEnBD.setRut("11111111-1");
        clienteEnBD.setNombreCompleto("Jim Halpert");

        Cliente datosNuevos = new Cliente();
        datosNuevos.setNombreCompleto("James Halpert"); // Corrección de nombre

        when(clienteRepository.findById(idActualizar)).thenReturn(Optional.of(clienteEnBD));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(i -> i.getArgument(0));

        ClienteDTO resultado = clienteService.actualizar(idActualizar, datosNuevos);

        assertNotNull(resultado);
        assertEquals("James Halpert", clienteEnBD.getNombreCompleto());
        assertEquals("James Halpert", resultado.getNombreCompleto());
        verify(clienteRepository, times(1)).save(clienteEnBD);
    }
}
