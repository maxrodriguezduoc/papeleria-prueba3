package Ubicacion.Local.config;

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
            .title("API de Locales y Sucursales - The Office")
            .version("1.0")
            .description("Con esta API se puede administrar la red de sucursales de la papelería The Office, incluyendo la gestión y asignación de ubicaciones geográficas como regiones y comunas.")
        );
    }
}