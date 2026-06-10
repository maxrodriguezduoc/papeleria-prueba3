package Ubicacion.Local.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> manejarErroresDeNegocio(RuntimeException e) {
        log.error("Excepción de negocio capturada: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> manejarErroresDeValidacion(MethodArgumentNotValidException e) {
        String mensaje = "Datos de entrada inválidos.";
        
        // Extraemos el primer mensaje de error específico de la validación
        if (e.getBindingResult().getFieldError() != null) {
            mensaje = e.getBindingResult().getFieldError().getDefaultMessage();
        }
        
        log.error("Error de validación (400 Bad Request): {}", mensaje);
        return new ResponseEntity<>(mensaje, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> manejarErroresGenerales(Exception e) {
        log.error("ERROR CRÍTICO NO CONTROLADO: ", e);
        return new ResponseEntity<>("Ocurrió un error inesperado en el servidor.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}