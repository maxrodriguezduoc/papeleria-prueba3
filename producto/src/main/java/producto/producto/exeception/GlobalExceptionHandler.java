package producto.producto.exeception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // 1. CAPTURAR ERRORES DE LÓGICA DE NEGOCIO (Ej: "Cliente no existe")
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> manejarErroresDeNegocio(RuntimeException e) {
        log.error("Excepción de negocio capturada: {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    // 2. CAPTURAR ERRORES DE VALIDACIÓN
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

    // 3. CAPTURAR ERRORES INESPERADOS DEL SISTEMA
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> manejarErroresGenerales(Exception e) {
        log.error("ERROR CRÍTICO NO CONTROLADO: ", e);
        return new ResponseEntity<>("Ocurrió un error inesperado en el servidor.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
