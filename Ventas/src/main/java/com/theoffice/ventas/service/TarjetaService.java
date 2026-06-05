package com.theoffice.ventas.service;

import com.theoffice.ventas.DTO.TarjetaDTO;
import com.theoffice.ventas.model.Tarjeta;
import com.theoffice.ventas.repository.TarjetaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

@Service
@Transactional
@Slf4j
public class TarjetaService {

    @Autowired
    private TarjetaRepository tarjetaRepository;

    public TarjetaDTO crear(Tarjeta tarjeta) {
        log.info("Intentando registrar una nueva tarjeta: {}", enmascararNumero(tarjeta.getNumeroTarjeta()));

        if (!validarNumeroTarjeta(tarjeta.getNumeroTarjeta())) {
            log.error("Falla al crear: El número de tarjeta es inválido");
            throw new RuntimeException("El número de tarjeta es inválido o no cumple con el formato estándar.");
        }

        if (!validarCvv(tarjeta.getCvv())) {
            throw new RuntimeException("El código CVV no es válido.");
        }

        if (!validarFechaExpiracion(tarjeta.getFechaExpiracion())) {
            throw new RuntimeException("La tarjeta se encuentra vencida o el formato de fecha es incorrecto.");
        }

        if (tarjeta.getNombreBanco() == null || tarjeta.getNombreBanco().trim().isEmpty()) {
            log.error("Falla al crear: El nombre del banco está vacío");
            throw new RuntimeException("El nombre del banco es obligatorio.");
        }

        tarjeta.setNombreBanco(tarjeta.getNombreBanco().trim());
        tarjeta.setActivo(true);

        Tarjeta guardada = tarjetaRepository.save(tarjeta);
        log.info("Tarjeta registrada con éxito. ID asignado: {}", guardada.getIdTarjeta());

        return convertirADTO(guardada);
    }

    public List<TarjetaDTO> obtenerTodas() {
        log.info("Consultando el listado de tarjetas activas");
        
        return tarjetaRepository.findAll().stream()
                .filter(Tarjeta::isActivo)
                .map(this::convertirADTO)
                .toList();
    }

    public TarjetaDTO obtenerPorId(Integer id) {
        log.info("Buscando tarjeta con ID: {}", id);
        
        Tarjeta tarjeta = tarjetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la tarjeta con ID: " + id));

        if (!tarjeta.isActivo()) {
            log.warn("La tarjeta ID {} existe pero está marcada como inactiva (baja lógica)", id);
            throw new RuntimeException("La tarjeta solicitada ya no está disponible.");
        }

        return convertirADTO(tarjeta);
    }

    public TarjetaDTO actualizar(Integer id, Tarjeta tarjeta) {
        log.info("Iniciando actualización para la tarjeta ID: {}", id);
        
        Tarjeta existente = tarjetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar: Tarjeta no encontrada."));

        if (!existente.isActivo()) {
            throw new RuntimeException("No se puede modificar una tarjeta que está inactiva.");
        }

        if (tarjeta.getNumeroTarjeta() != null && !tarjeta.getNumeroTarjeta().equals(existente.getNumeroTarjeta())) {
            if (!validarNumeroTarjeta(tarjeta.getNumeroTarjeta())) {
                throw new RuntimeException("El nuevo número de tarjeta ingresado no es válido.");
            }
            existente.setNumeroTarjeta(tarjeta.getNumeroTarjeta());
        }

        if (tarjeta.getNombreBanco() == null || tarjeta.getNombreBanco().trim().isEmpty()) {
            throw new RuntimeException("El nombre del banco no puede estar vacío.");
        }

        existente.setNombreBanco(tarjeta.getNombreBanco().trim());
        existente.setNombreTitular(tarjeta.getNombreTitular());
        existente.setCvv(tarjeta.getCvv());
        log.info("Tarjeta ID {} modificada con éxito", id);

        return convertirADTO(existente);
    }

    public void eliminar(Integer id) {
        log.warn("Procesando baja lógica para la tarjeta ID: {}", id);
        
        Tarjeta tarjeta = tarjetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede eliminar: El ID ingresado no existe."));

        if (!tarjeta.isActivo()) {
            log.info("La tarjeta ID {} ya se encontraba inactiva", id);
            return;
        }

        tarjeta.setActivo(false);
        tarjetaRepository.save(tarjeta);
        
        log.info("La tarjeta ID {} ha sido desactivada correctamente", id);
    }

    private boolean validarNumeroTarjeta(String numero) {
        if (numero == null) {
            return false;
        }
        String limpio = numero.replace(" ", "").replace("-", "");
        
        // se exige la expresion regular con digitos de 0 a 9 y de 13 a 19 digitos
        return limpio.matches("^[0-9]{13,19}$");
    }

    private String enmascararNumero(String numero) {
        if (numero == null || numero.length() < 4) {
            return "****";
        }
        // Retorna el número ocultando los primeros dígitos por seguridad del cliente
        return "****-****-****-" + numero.substring(numero.length() - 4);
    }

    private boolean validarCvv(String cvv) {
        if (cvv == null) {
        log.warn("Falla: El CVV es nulo");
        return false;
        }
        
        log.info("El CVV es valido");
        return cvv.matches("^[0-9]{3}$");   
    }

    private boolean validarFechaExpiracion(String fecha) {
    // 1. Validar formato básico (ej: "05/26")
    if (fecha == null || !fecha.matches("^(0[1-9]|1[0-2])/[0-9]{2}$")) {
        log.warn("Falla: Formato de fecha inválido '{}'", fecha);
        return false;
    }

    // 2. Separar mes y año
    String[] partes = fecha.split("/");
    Integer mesTarjeta = Integer.valueOf(partes[0]);
    Integer anioTarjeta = Integer.valueOf(partes[1]); // Ej: 26 (para 2026), 29 (para 2029)

    // 3. Obtener año y mes actual usando Calendar
    Calendar calendario = Calendar.getInstance();
    Integer anioActual = calendario.get(Calendar.YEAR) % 100; // Obtiene los últimos 2 dígitos (ej: 26)
    Integer mesActual = calendario.get(Calendar.MONTH) + 1;   // En Calendar, Enero es 0, por eso sumamos 1

    if (anioTarjeta < anioActual) {
        log.error("Tarjeta vencida por año: {} es anterior a {}", anioTarjeta, anioActual);
        return false;
    }

    if (anioTarjeta.equals(anioActual) && mesTarjeta < mesActual) {
        log.error("Tarjeta vencida este año: Mes {} es anterior a {}", mesTarjeta, mesActual);
        return false;
    }

    log.info("La fecha de expiración {} es válida", fecha);
    return true;
}

    private TarjetaDTO convertirADTO(Tarjeta tarjeta) {
        TarjetaDTO dto = new TarjetaDTO();
        dto.setIdTarjeta(tarjeta.getIdTarjeta());
        dto.setUltimosCuatro(enmascararNumero(tarjeta.getNumeroTarjeta()));
        dto.setNombreBanco(tarjeta.getNombreBanco());
        dto.setNombreTitular(tarjeta.getNombreTitular());
        dto.setActivo(tarjeta.isActivo());
        return dto;
    }
}