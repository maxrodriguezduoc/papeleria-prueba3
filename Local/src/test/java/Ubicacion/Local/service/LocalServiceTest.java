package Ubicacion.Local.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
}