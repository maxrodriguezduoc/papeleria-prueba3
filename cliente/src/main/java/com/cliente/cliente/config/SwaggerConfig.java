package com.cliente.cliente.config;

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
            .title("API de Clientes - The Office")
            .version("1.0")
            .description("Con esta API se pueden administrar los clientes de la papelería The Office, incluyendo el registro, actualización, búsqueda y gestión de los perfiles de usuario y su información de contacto.")
        );
    }
}