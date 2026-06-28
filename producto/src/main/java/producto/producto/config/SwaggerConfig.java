package producto.producto.config;

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
            .title("API de Productos - The Office")
            .version("1.0")
            .description("Con esta API se puede administrar el catálogo de productos y el inventario de la papelería The Office, incluyendo la creación, actualización, listado y eliminación de artículos.")
        );
    }
}