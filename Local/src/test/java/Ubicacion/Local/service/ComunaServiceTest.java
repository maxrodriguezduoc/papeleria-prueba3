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

import Ubicacion.Local.dto.ComunaDTO;
import Ubicacion.Local.model.Comuna;
import Ubicacion.Local.model.Region;
import Ubicacion.Local.repository.ComunaRepository;
import Ubicacion.Local.repository.RegionRepository;

@ExtendWith(MockitoExtension.class)
class ComunaServiceTest {

    @Mock
    private ComunaRepository comunaRepository;

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private ComunaService comunaService;

    @Test
    void testGuardarComuna() {

        Region region = new Region();
        region.setIdRegion(1);
        region.setActivo(true);

        Comuna comuna = new Comuna();
        comuna.setNombreComuna("Maipú");
        comuna.setCodigoPostal("8320000");
        comuna.setActivo(true);
        comuna.setRegion(region);

        when(regionRepository.findById(1))
                .thenReturn(Optional.of(region));

        when(comunaRepository.save(any(Comuna.class)))
                .thenAnswer(inv -> {
                    Comuna c = inv.getArgument(0);
                    c.setIdComuna(1);
                    return c;
                });

        ComunaDTO resultado = comunaService.guardarComuna(comuna);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdComuna());
        assertEquals("Maipú", resultado.getNombreComuna());
        assertEquals("8320000", resultado.getCodigoPostal());
        assertEquals(1, resultado.getRegionId());

        verify(comunaRepository, times(1)).save(any(Comuna.class));
    }

    @Test
    void testBuscarPorId() {

        Integer id = 1;

        Region region = new Region();
        region.setIdRegion(1);

        Comuna comuna = new Comuna();
        comuna.setIdComuna(id);
        comuna.setNombreComuna("Santiago");
        comuna.setCodigoPostal("8320000");
        comuna.setActivo(true);
        comuna.setRegion(region);

        when(comunaRepository.findById(id))
                .thenReturn(Optional.of(comuna));

        ComunaDTO resultado = comunaService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getIdComuna());
        assertEquals("Santiago", resultado.getNombreComuna());

        verify(comunaRepository, times(1)).findById(id);
    }

    @Test
    void testObtenerTodos() {

        Region region = new Region();
        region.setIdRegion(1);

        Comuna comuna = new Comuna();
        comuna.setIdComuna(1);
        comuna.setNombreComuna("Providencia");
        comuna.setCodigoPostal("8320000");
        comuna.setActivo(true);
        comuna.setRegion(region);

        when(comunaRepository.findAll())
                .thenReturn(List.of(comuna));

        List<ComunaDTO> resultado = comunaService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Providencia", resultado.get(0).getNombreComuna());

        verify(comunaRepository, times(1)).findAll();
    }

    @Test
    void testEliminarComuna() {

        Comuna comuna = new Comuna();
        comuna.setIdComuna(1);
        comuna.setActivo(true);

        when(comunaRepository.findById(1))
                .thenReturn(Optional.of(comuna));

        when(comunaRepository.save(any(Comuna.class)))
                .thenReturn(comuna);

        comunaService.eliminarComuna(1);

        assertFalse(comuna.isActivo());

        verify(comunaRepository, times(1)).findById(1);
        verify(comunaRepository, times(1)).save(comuna);
    }

    @Test
    void testActualizarComuna() {

        Integer id = 1;

        Region region = new Region();
        region.setIdRegion(1);
        region.setActivo(true);

        Comuna existente = new Comuna();
        existente.setIdComuna(id);
        existente.setNombreComuna("Antigua");
        existente.setCodigoPostal("1111111");
        existente.setActivo(true);
        existente.setRegion(region);

        Comuna nuevosDatos = new Comuna();
        nuevosDatos.setNombreComuna("Nueva Comuna");
        nuevosDatos.setCodigoPostal("2222222");
        nuevosDatos.setRegion(region);

        when(comunaRepository.findById(id))
                .thenReturn(Optional.of(existente));

        when(regionRepository.findById(1))
                .thenReturn(Optional.of(region));

        ComunaDTO resultado = comunaService.actualizarComuna(id, nuevosDatos);

        assertNotNull(resultado);
        assertEquals("Nueva Comuna", resultado.getNombreComuna());
        assertEquals("2222222", resultado.getCodigoPostal());

        verify(comunaRepository, times(1)).findById(id);
    }
}