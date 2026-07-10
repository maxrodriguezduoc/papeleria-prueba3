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

import Ubicacion.Local.dto.LocalDTO;
import Ubicacion.Local.model.Comuna;
import Ubicacion.Local.model.Local;
import Ubicacion.Local.repository.LocalRepository;

@ExtendWith(MockitoExtension.class)
class LocalServiceTest {

    @Mock
    private LocalRepository localRepository;

    @InjectMocks
    private LocalService localService;

    @Test
    void testGuardarLocal() {

        Comuna comuna = new Comuna();
        comuna.setIdComuna(1);
        comuna.setNombreComuna("Cerrillos");

        Local local = new Local();
        local.setNombreLocal("Sucursal Plaza Oeste");
        local.setDireccion("Av. Americo Vespucio 1501");
        local.setActivo(true);
        local.setComuna(comuna);

        when(localRepository.save(any(Local.class))).thenAnswer(inv -> {
            Local l = inv.getArgument(0);
            l.setIdLocal(1);
            return l;
        });

        LocalDTO resultado = localService.guardarLocal(local);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdLocal());
        assertEquals("Sucursal Plaza Oeste", resultado.getNombreLocal());
        assertEquals("Av. Americo Vespucio 1501", resultado.getDireccion());
        assertEquals(1, resultado.getComunaId());

        verify(localRepository, times(1)).save(any(Local.class));
    }

    @Test
    void testBuscarPorId() {

        Integer id = 1;

        Comuna comuna = new Comuna();
        comuna.setIdComuna(1);
        comuna.setNombreComuna("Cerrillos");

        Local local = new Local();
        local.setIdLocal(id);
        local.setNombreLocal("Sucursal Plaza Oeste");
        local.setDireccion("Av. Americo Vespucio 1501");
        local.setActivo(true);
        local.setComuna(comuna);

        when(localRepository.findById(id)).thenReturn(Optional.of(local));

        LocalDTO resultado = localService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getIdLocal());
        assertEquals("Sucursal Plaza Oeste", resultado.getNombreLocal());
        assertEquals("Av. Americo Vespucio 1501", resultado.getDireccion());
        assertEquals(1, resultado.getComunaId());

        verify(localRepository, times(1)).findById(id);
    }

    @Test
    void testObtenerTodos() {

        Comuna comuna = new Comuna();
        comuna.setIdComuna(1);
        comuna.setNombreComuna("Cerrillos");

        Local local = new Local();
        local.setIdLocal(1);
        local.setNombreLocal("Sucursal Plaza Oeste");
        local.setDireccion("Av. Americo Vespucio 1501");
        local.setActivo(true);
        local.setComuna(comuna);

        when(localRepository.findAll()).thenReturn(List.of(local));

        List<LocalDTO> resultado = localService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Sucursal Plaza Oeste", resultado.get(0).getNombreLocal());

        verify(localRepository, times(1)).findAll();
    }

    @Test
    void testEliminarLocal() {

        Integer id = 1;

        Comuna comuna = new Comuna();
        comuna.setIdComuna(1);

        Local local = new Local();
        local.setIdLocal(id);
        local.setNombreLocal("Sucursal Plaza Oeste");
        local.setDireccion("Av. Americo Vespucio");
        local.setActivo(true);
        local.setComuna(comuna);

        when(localRepository.findById(id)).thenReturn(Optional.of(local));
        when(localRepository.save(any(Local.class))).thenReturn(local);

        localService.eliminarLocal(id);

        assertFalse(local.isActivo());

        verify(localRepository, times(1)).findById(id);
        verify(localRepository, times(1)).save(local);
    }

    @Test
    void testActualizarLocal() {

        Integer id = 1;

        Comuna comuna = new Comuna();
        comuna.setIdComuna(1);
        comuna.setNombreComuna("Cerrillos");

        Local existente = new Local();
        existente.setIdLocal(id);
        existente.setNombreLocal("Sucursal Antigua");
        existente.setDireccion("Direccion Antigua");
        existente.setActivo(true);
        existente.setComuna(comuna);

        Local nuevosDatos = new Local();
        nuevosDatos.setNombreLocal("Sucursal Nueva");
        nuevosDatos.setDireccion("Direccion Nueva");
        nuevosDatos.setComuna(comuna);

        when(localRepository.findById(id)).thenReturn(Optional.of(existente));
        when(localRepository.save(any(Local.class))).thenAnswer(inv -> inv.getArgument(0));

        LocalDTO resultado = localService.actualizarLocal(id, nuevosDatos);

        assertNotNull(resultado);
        assertEquals("Sucursal Nueva", resultado.getNombreLocal());
        assertEquals("Direccion Nueva", resultado.getDireccion());

        verify(localRepository, times(1)).findById(id);
        verify(localRepository, times(1)).save(any(Local.class));
    }
}