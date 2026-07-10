package com.theoffice.ventas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI().info(
            new Info()
            .title("API de Ventas y Transacciones - The Office")
            .version("1.0")
            .description("Microservicio encargado de gestionar las ventas, pagos, tarjetas y transferencias de la papelería The Office. Incluye soporte para HATEOAS.")
        );
    }
}