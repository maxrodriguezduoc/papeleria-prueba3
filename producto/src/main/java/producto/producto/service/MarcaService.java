package producto.producto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import producto.producto.dto.MarcaDTO;
import producto.producto.model.Marca;
import producto.producto.repository.MarcaRepository;

@Slf4j
@Service
@Transactional
public class MarcaService {
    @Autowired
    private MarcaRepository marcaRepository;

    public MarcaDTO crearMarca(Marca marcas){
        log.info("Intetando registrar una nueva marca: {}", marcas.getNombre_marca());
        if (marcas.getNombre_marca() == null || marcas.getNombre_marca().trim().isEmpty()) {
            log.error("Falla al crear: El nombre está vacío");
            throw new RuntimeException("El nombre de la marca es obligatorio.");
        }

        marcas.setNombre_marca(marcas.getNombre_marca().trim());
        marcas.setActivo(true);
        marcaRepository.save(marcas);

        log.info("Marca registrada con exito. Id asignado para la marca es: {}", marcas.getId_marcas());
        return convertirADTO(marcas);
    }

    public List<MarcaDTO> obtenerTodos(){
        log.info("Consultando listado de marcas activas");
        return marcaRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }


    public MarcaDTO buscarPorId(Integer id){
        log.info("Buscando marca con el ID: {}", id);
        Marca marca = marcaRepository.findById(id)
            .orElseThrow(()-> new RuntimeException("¡Marca no encontrado!"));
        return convertirADTO(marca);

    }

    public void eliminarMarca(Integer id){
        log.info("Esta eliminando la marca con el ID: {}", id);
        Marca marca = marcaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID no encontrado"));

        marca.setActivo(false);
        marcaRepository.save(marca);
    }    

    public MarcaDTO actualizarMarca(Integer id, Marca marca){
        log.info("Actualizando marca con ID: {}", id);
        Marca marcas = marcaRepository.findById(id).orElseThrow(() -> new RuntimeException("No existe la marca con el ID:" + id));

        if(marcas.getNombre_marca()!= null){
            marca.setNombre_marca(marca.getNombre_marca().trim());
        }
        return convertirADTO(marca);

    }
    
    private MarcaDTO convertirADTO(Marca marcas){
        MarcaDTO dto = new MarcaDTO();
        dto.setId_marcas(marcas.getId_marcas());
        dto.setNombre_marca(marcas.getNombre_marca());
        return dto;
    }
}
