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

import Ubicacion.Local.dto.ColaboradorDTO;
import Ubicacion.Local.model.Cargo;
import Ubicacion.Local.model.Colaborador;
import Ubicacion.Local.model.Colaboradores;
import Ubicacion.Local.model.Local;
import Ubicacion.Local.repository.CargoRepository;
import Ubicacion.Local.repository.ColaboradorRepository;
import Ubicacion.Local.repository.LocalRepository;

@ExtendWith(MockitoExtension.class)
class ColaboradorServiceTest {

    @Mock
    private ColaboradorRepository colaboradorRepository;

    @Mock
    private CargoRepository cargoRepository;

    @Mock
    private LocalRepository localRepository;

    @InjectMocks
    private ColaboradorService colaboradorService;

    @Test
    void testGuardarColaborador() {

        Cargo cargo = new Cargo();
        cargo.setIdCargo(1);
        cargo.setActivo(true);

        Local local = new Local();
        local.setIdLocal(1);
        local.setActivo(true);

        Colaboradores puente = new Colaboradores();
        puente.setLocal(local);

        Colaborador colaborador = new Colaborador();
        colaborador.setNombreColaborador("Juan Perez");
        colaborador.setActivo(true);
        colaborador.setCargo(cargo);
        colaborador.setLocales(List.of(puente));

        when(cargoRepository.findById(1)).thenReturn(Optional.of(cargo));
        when(localRepository.findById(1)).thenReturn(Optional.of(local));

        when(colaboradorRepository.save(any(Colaborador.class)))
                .thenAnswer(inv -> {
                    Colaborador c = inv.getArgument(0);
                    c.setIdColaborador(1);
                    return c;
                });

        ColaboradorDTO resultado = colaboradorService.guardarColaborador(colaborador);

        assertNotNull(resultado);
        assertEquals(1, resultado.getIdColaborador());
        assertEquals("Juan Perez", resultado.getNombreColaborador());
        assertEquals(1, resultado.getCargoId());
        assertEquals(1, resultado.getLocalId());

        verify(colaboradorRepository, times(1)).save(any(Colaborador.class));
    }

    @Test
    void testBuscarPorId() {

        Integer id = 1;

        Colaborador colaborador = new Colaborador();
        colaborador.setIdColaborador(id);
        colaborador.setNombreColaborador("Pedro");
        colaborador.setActivo(true);

        when(colaboradorRepository.findById(id))
                .thenReturn(Optional.of(colaborador));

        ColaboradorDTO resultado = colaboradorService.buscarPorId(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getIdColaborador());
        assertEquals("Pedro", resultado.getNombreColaborador());

        verify(colaboradorRepository, times(1)).findById(id);
    }

    @Test
    void testObtenerTodos() {

        Colaborador colaborador = new Colaborador();
        colaborador.setIdColaborador(1);
        colaborador.setNombreColaborador("Ana");
        colaborador.setActivo(true);

        when(colaboradorRepository.findAll())
                .thenReturn(List.of(colaborador));

        List<ColaboradorDTO> resultado = colaboradorService.obtenerTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Ana", resultado.get(0).getNombreColaborador());

        verify(colaboradorRepository, times(1)).findAll();
    }

    @Test
    void testEliminarColaborador() {

        Integer id = 1;

        Colaborador colaborador = new Colaborador();
        colaborador.setIdColaborador(id);
        colaborador.setActivo(true);

        when(colaboradorRepository.findById(id))
                .thenReturn(Optional.of(colaborador));

        when(colaboradorRepository.save(any(Colaborador.class)))
                .thenReturn(colaborador);

        colaboradorService.eliminarColaborador(id);

        assertFalse(colaborador.isActivo());

        verify(colaboradorRepository, times(1)).findById(id);
        verify(colaboradorRepository, times(1)).save(colaborador);
    }

    @Test
    void testActualizarColaborador() {

        Integer id = 1;

        Cargo cargo = new Cargo();
        cargo.setIdCargo(1);
        cargo.setActivo(true);

        Local local = new Local();
        local.setIdLocal(1);
        local.setActivo(true);

        Colaboradores puente = new Colaboradores();
        puente.setLocal(local);

        Colaborador existente = new Colaborador();
        existente.setIdColaborador(id);
        existente.setNombreColaborador("Antiguo");
        existente.setActivo(true);
        existente.setCargo(cargo);
        existente.setLocales(List.of(puente));

        Colaboradores nuevoPuente = new Colaboradores();
        nuevoPuente.setLocal(local);

        Colaborador nuevosDatos = new Colaborador();
        nuevosDatos.setNombreColaborador("Nuevo Nombre");
        nuevosDatos.setCargo(cargo);
        nuevosDatos.setLocales(List.of(nuevoPuente));

        when(colaboradorRepository.findById(id))
                .thenReturn(Optional.of(existente));

        when(cargoRepository.findById(1))
                .thenReturn(Optional.of(cargo));

        when(localRepository.findById(1))
                .thenReturn(Optional.of(local));

        when(colaboradorRepository.save(any(Colaborador.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        ColaboradorDTO resultado = colaboradorService.actualizarColaborador(id, nuevosDatos);

        assertNotNull(resultado);
        assertEquals("Nuevo Nombre", resultado.getNombreColaborador());

        verify(colaboradorRepository, times(1)).findById(id);
        verify(colaboradorRepository, times(1)).save(any(Colaborador.class));
    }
}