package com.cliente.cliente.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cliente.cliente.dto.ClienteDTO;
import com.cliente.cliente.model.Cliente;
import com.cliente.cliente.repository.ClienteRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;

    public ClienteDTO crear(Cliente cliente) {
        log.info("Intentando registrar un nuevo cliente con RUT: {}", cliente.getRut());

        if (!validarRutMatematico(cliente.getRut())) {
            log.error("Falla al crear: El RUT {} es matemáticamente inválido", cliente.getRut());
            throw new RuntimeException("El RUT ingresado no es válido.");
        }

        cliente.setActivo(true);

        Cliente guardado = clienteRepository.save(cliente);
        log.info("Cliente registrado con éxito en TheOffice. ID asignado: {}", guardado.getIdCliente());

        return convertirADTO(guardado);
    }

    public List<ClienteDTO> obtenerTodos() {
        log.info("Consultando el listado de clientes activos");
        
        return clienteRepository.findAll().stream()
                .filter(Cliente::isActivo)
                .map(this::convertirADTO)
                .toList();
    }

    public ClienteDTO obtenerPorId(Integer id) {
        log.info("Buscando cliente con ID: {}", id);
        
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el cliente con ID: " + id));

        if (!cliente.isActivo()) {
            log.warn("El cliente ID {} existe pero está marcado como inactivo", id);
            throw new RuntimeException("El cliente solicitado ya no está disponible.");
        }

        return convertirADTO(cliente);
    }

    public ClienteDTO actualizar(Integer id, Cliente cliente) {
        log.info("Iniciando actualización para el cliente ID: {}", id);
        
        Cliente existente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar: Cliente no encontrado."));

        if (!existente.isActivo()) {
            throw new RuntimeException("No se puede modificar un cliente que está inactivo.");
        }

        if (cliente.getRut() != null && !cliente.getRut().equals(existente.getRut())) {
            if (!validarRutMatematico(cliente.getRut())) {
                throw new RuntimeException("El nuevo RUT ingresado no es válido.");
            }
            existente.setRut(cliente.getRut());
        }

        existente.setNombreCompleto(cliente.getNombreCompleto());

        log.info("Cliente ID {} modificado con éxito", id);

        return convertirADTO(existente);
    }

    public void eliminar(Integer id) {
        log.warn("Procesando baja lógica para el cliente ID: {}", id);
        
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede eliminar: El ID ingresado no existe."));

        if (!cliente.isActivo()) {
            log.info("El cliente ID {} ya se encontraba inactivo", id);
            return;
        }

        cliente.setActivo(false);
        clienteRepository.save(cliente);
        
        log.info("El cliente ID {} ha sido desactivado correctamente", id);
    }

    private boolean validarRutMatematico(String rut) {
        if (rut == null || rut.isEmpty()) return false;

        try {
            log.info("Iniciando validación matemática de RUT: {}", rut);
            
            // Limpieza y normalización
            rut = rut.toUpperCase().replace(".", "").replace("-", "");
            if (rut.length() < 2) return false;

            // Separar cuerpo y dígito verificador
            String cuerpo = rut.substring(0, rut.length() - 1);
            Character dvIngresado = rut.charAt(rut.length() - 1);

            // Algoritmo verificación rut
            Integer suma = 0;
            Integer multiplicador = 2;

            for (int i = cuerpo.length() - 1; i >= 0; i--) {
                suma += Character.getNumericValue(cuerpo.charAt(i)) * multiplicador;
                multiplicador = (multiplicador == 7) ? 2 : multiplicador + 1;
            }

            Integer resto = suma % 11;
            Integer resultado = 11 - resto;

            Character dvCalculado;
            if (resultado == 11) {
                dvCalculado = '0';
            } else if (resultado == 10) {
                dvCalculado = 'K';
            } else {
                dvCalculado = Character.forDigit(resultado, 10);
            }

            // Comparación final
            Boolean esValido = (dvCalculado == dvIngresado);
            
            if (esValido) {
                log.info("Validación exitosa para RUT: {}", rut);
            } else {
                log.error("Error: El DV calculado ({}) no coincide con el ingresado ({})", dvCalculado, dvIngresado);
            }

            return esValido;

        } catch (Exception e) {
            log.error("Error procesando el RUT: {}", e.getMessage());
            return false;
        }
    }

    private ClienteDTO convertirADTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setIdCliente(cliente.getIdCliente());
        dto.setRut(cliente.getRut());
        dto.setNombreCompleto(cliente.getNombreCompleto());
        return dto;
    }
}
