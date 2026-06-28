package Ubicacion.Local.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import Ubicacion.Local.dto.CargoDTO;
import Ubicacion.Local.model.Cargo;
import Ubicacion.Local.repository.CargoRepository;

@ExtendWith(MockitoExtension.class)
class CargoServiceTest {

    @Mock
    private CargoRepository cargoRepository;

    @InjectMocks
    private CargoService cargoService;

    @Test
    void testGuardarCargo() {

        Cargo cargo = new Cargo();
        cargo.setNombreCargo("Jefe de Tienda");
        cargo.setActivo(true);

        when(cargoRepository.save(any(Cargo.class))).thenAnswer(inv -> {
            Cargo c = inv.getArgument(0);
            c.setIdCargo(1);
            return c;
        });

        CargoDTO resultado = cargoService.guardarCargo(cargo);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdCargo());
        assertEquals("Jefe de Tienda", resultado.getNombreCargo());

        verify(cargoRepository, times(1)).save(any(Cargo.class));
    }

    @Test
    void testObtenerTodos() {

        Cargo cargo = new Cargo();
        cargo.setIdCargo(1);
        cargo.setNombreCargo("Vendedor");
        cargo.setActivo(true);

        when(cargoRepository.findAll()).thenReturn(List.of(cargo));

        List<CargoDTO> resultado = cargoService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Vendedor", resultado.get(0).getNombreCargo());

        verify(cargoRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId() {

        Integer id = 1;

        Cargo cargo = new Cargo();
        cargo.setIdCargo(id);
        cargo.setNombreCargo("Cajero");
        cargo.setActivo(true);

        when(cargoRepository.findById(id)).thenReturn(Optional.of(cargo));

        CargoDTO resultado = cargoService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getIdCargo());
        assertEquals("Cajero", resultado.getNombreCargo());

        verify(cargoRepository, times(1)).findById(id);
    }

    @Test
    void testEliminarCargo() {

        Integer id = 1;

        Cargo cargo = new Cargo();
        cargo.setIdCargo(id);
        cargo.setNombreCargo("Reponedor");
        cargo.setActivo(true);

        when(cargoRepository.findById(id)).thenReturn(Optional.of(cargo));
        when(cargoRepository.save(any(Cargo.class))).thenReturn(cargo);

        cargoService.eliminarCargo(id);

        assertFalse(cargo.isActivo());

        verify(cargoRepository, times(1)).findById(id);
        verify(cargoRepository, times(1)).save(cargo);
    }

    @Test
    void testActualizarCargo() {

        Integer id = 1;

        Cargo existente = new Cargo();
        existente.setIdCargo(id);
        existente.setNombreCargo("Antiguo Cargo");
        existente.setActivo(true);

        Cargo nuevosDatos = new Cargo();
        nuevosDatos.setNombreCargo("Cargo Actualizado");

        when(cargoRepository.findById(id)).thenReturn(Optional.of(existente));
        when(cargoRepository.save(any(Cargo.class))).thenAnswer(inv -> inv.getArgument(0));

        CargoDTO resultado = cargoService.actualizarCargo(id, nuevosDatos);

        assertNotNull(resultado);
        assertEquals("Cargo Actualizado", resultado.getNombreCargo());

        verify(cargoRepository, times(1)).findById(id);
        verify(cargoRepository, times(1)).save(any(Cargo.class));
    }
}