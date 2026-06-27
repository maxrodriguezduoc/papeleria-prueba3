package Ubicacion.Local.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import Ubicacion.Local.model.Region;
import Ubicacion.Local.repository.LocalRepository;

@ExtendWith(MockitoExtension.class)
public class LocalServiceTest {

    @Mock
    private LocalRepository localRepository;

    @InjectMocks
    private LocalService localService;

    @Test
    void testGuardadoExitosoLocal(){

        Comuna comuna = new Comuna();
        comuna.setIdComuna(1);
        comuna.setNombreComuna("Cerrillos");
        comuna.setCodigoPostal("8320000");

        Local localFalso = new Local();
        localFalso.setIdLocal(1);
        localFalso.setNombreLocal("Sucursal Plaza Oeste");
        localFalso.setDireccion("Av. Americo Vespucio 1501");
        localFalso.setActivo(true);
        localFalso.setComuna(comuna);

        when(localRepository.save(any(Local.class)))
                .thenReturn(localFalso);

        LocalDTO resultado = localService.guardarLocal(localFalso);

        assertNotNull(resultado,
                "El DTO resultante no deberia ser nulo!");
        
        assertEquals("Sucursal Plaza Oeste",
                resultado.getNombreLocal(),
                "El nombre de Local debe coincidir!");

        assertEquals("Av. Americo Vespucio 1501", 
                resultado.getDireccion(),
                "La direccion de Local debe coincidir!");

        assertEquals(1,
                resultado.getComunaId(),
                "La comuna de Local debe coincidir!");

        verify(localRepository, atLeastOnce())
                .save(any(Local.class));
    }

    @Test
    void testBuscarPorId(){

        Integer idBuscado = 1;

        Region region = new Region();
        region.setIdRegion(1);
        region.setNombreRegion("Region Metropolitana");
        region.setActivo(true);

        Comuna comuna = new Comuna();
        comuna.setIdComuna(1);
        comuna.setNombreComuna("Cerrillos");
        comuna.setCodigoPostal("8320000");

        Local localFalso = new Local();
        localFalso.setIdLocal(1);
        localFalso.setNombreLocal("Sucursal Plaza Oeste");
        localFalso.setDireccion("Av. Americo Vespucio 1501");
        localFalso.setActivo(true);
        localFalso.setComuna(comuna);

        when(localRepository.findById(idBuscado))
                .thenReturn(Optional.of(localFalso));

        LocalDTO resultado = localService.buscarPorId(idBuscado);

        assertNotNull(resultado,
                "El DTO resultante no deberia ser nulo!");

        assertEquals(idBuscado,
                resultado.getIdLocal(),
                "La ID del Local debe coincidir!");

        assertEquals("Sucursal Plaza Oeste", 
                resultado.getNombreLocal(),
                "El nombre del Local debe coincidir!");

        assertEquals("Av. Americo Vespucio 1501", 
                resultado.getDireccion(),
                "La direccion del Local debe coincidir!");

        assertEquals(1, 
                resultado.getComunaId(),
                "La comuna del Local debe coincidir!");
        verify(localRepository, atLeastOnce())
            .findById(idBuscado);
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

        when(localRepository.findAll())
                .thenReturn(List.of(local));

        List<LocalDTO> resultado = localService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Sucursal Plaza Oeste", resultado.get(0).getNombreLocal());

        verify(localRepository).findAll();
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

        when(localRepository.findById(id))
                .thenReturn(Optional.of(local));

        when(localRepository.save(any(Local.class)))
                .thenReturn(local);

        localService.eliminarLocal(id);

        assertEquals(false, local.isActivo());

        verify(localRepository).findById(id);
        verify(localRepository).save(local);
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

        when(localRepository.findById(id))
                .thenReturn(Optional.of(existente));

        when(localRepository.save(any(Local.class)))
                .thenReturn(existente);

        LocalDTO resultado = localService.actualizarLocal(id, nuevosDatos);

        assertNotNull(resultado);
        assertEquals("Sucursal Nueva", resultado.getNombreLocal());
        assertEquals("Direccion Nueva", resultado.getDireccion());

        verify(localRepository).findById(id);
        verify(localRepository).save(any(Local.class));
     }
}