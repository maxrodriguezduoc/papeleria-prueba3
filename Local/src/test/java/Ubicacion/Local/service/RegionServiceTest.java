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

import Ubicacion.Local.dto.RegionDTO;
import Ubicacion.Local.model.Region;
import Ubicacion.Local.repository.RegionRepository;

@ExtendWith(MockitoExtension.class)
class RegionServiceTest {

    @Mock
    private RegionRepository regionRepository;

    @InjectMocks
    private RegionService regionService;

    @Test
    void testGuardarRegion() {

        Region region = new Region();
        region.setNombreRegion("Región Metropolitana de Santiago");

        when(regionRepository.save(any(Region.class))).thenAnswer(inv -> {
            Region r = inv.getArgument(0);
            r.setIdRegion(1);
            r.setActivo(true);
            return r;
        });

        RegionDTO resultado = regionService.guardarRegion(region);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdRegion());
        assertEquals("Región Metropolitana de Santiago", resultado.getNombreRegion());
        assertTrue(resultado.isActivo());

        verify(regionRepository, times(1)).save(any(Region.class));
    }

    @Test
    void testBuscarPorId() {

        Integer id = 1;

        Region region = new Region();
        region.setIdRegion(id);
        region.setNombreRegion("Región Metropolitana de Santiago");
        region.setActivo(true);

        when(regionRepository.findById(id))
                .thenReturn(Optional.of(region));

        RegionDTO resultado = regionService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getIdRegion());
        assertEquals("Región Metropolitana de Santiago", resultado.getNombreRegion());

        verify(regionRepository, times(1)).findById(id);
    }

    @Test
    void testObtenerTodos() {

        Region region = new Region();
        region.setIdRegion(1);
        region.setNombreRegion("Región de Valparaíso");
        region.setActivo(true);

        when(regionRepository.findAll())
                .thenReturn(List.of(region));

        List<RegionDTO> resultado = regionService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Región de Valparaíso", resultado.get(0).getNombreRegion());

        verify(regionRepository, times(1)).findAll();
    }

    @Test
    void testEliminarRegion() {

        Region region = new Region();
        region.setIdRegion(1);
        region.setNombreRegion("Región del Biobío");
        region.setActivo(true);

        when(regionRepository.findById(1))
                .thenReturn(Optional.of(region));

        when(regionRepository.save(any(Region.class)))
                .thenReturn(region);

        regionService.eliminarRegion(1);

        assertFalse(region.isActivo());

        verify(regionRepository, times(1)).findById(1);
        verify(regionRepository, times(1)).save(region);
    }

    @Test
    void testActualizarRegion() {

        Integer id = 1;

        Region existente = new Region();
        existente.setIdRegion(id);
        existente.setNombreRegion("Región Antigua");
        existente.setActivo(true);

        Region nuevosDatos = new Region();
        nuevosDatos.setNombreRegion("Región Actualizada");

        when(regionRepository.findById(id))
                .thenReturn(Optional.of(existente));

        when(regionRepository.save(any(Region.class)))
                .thenReturn(existente);

        RegionDTO resultado = regionService.actualizarRegion(id, nuevosDatos);

        assertNotNull(resultado);
        assertEquals("Región Actualizada", resultado.getNombreRegion());

        verify(regionRepository, times(1)).findById(id);
        verify(regionRepository, times(1)).save(any(Region.class));
    }
}