package com.cliente.cliente; // Paquete principal de tu microservicio de clientes

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import net.datafaker.Faker;
import com.cliente.cliente.model.Cliente;
import com.cliente.cliente.repository.ClienteRepository;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public void run(String... args) throws Exception {

        Faker faker = new Faker();
        Random random = new Random();

        // Generar 25 clientes de prueba por defecto
        for (int i = 0; i < 25; i++) {
            Cliente cliente = new Cliente();
            
            // 1. Generamos un RUT que cumpla exactamente con la expresión regular: ^[0-9]{7,8}-[0-9Kk]$
            // Genera un número de 7 u 8 dígitos (ej: 17453211) más el guion y el verificador
            String digitosRut = String.valueOf(faker.number().numberBetween(1000000, 25000000));
            String dv = faker.options().option("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "K", "k");
            String rutValido = digitosRut + "-" + dv;
            
            cliente.setRut(rutValido);
            
            // 2. Generamos el nombre completo usando nombre y apellido para asegurar superar los 10 caracteres mínimos
            String nombreCompletoAleatorio = faker.name().firstName() + " " + faker.name().lastName();
            
            // Si por azar el nombre queda corto, le concatenamos un segundo apellido para blindar la validación de 10 caracteres
            if (nombreCompletoAleatorio.length() < 10) {
                nombreCompletoAleatorio += " " + faker.name().lastName();
            }
            
            cliente.setNombreCompleto(nombreCompletoAleatorio);
            
            // 3. Estado de baja lógica activo por defecto
            cliente.setActivo(true); 

            // Guardar en la base de datos de desarrollo
            clienteRepository.save(cliente);
        }
    }
}