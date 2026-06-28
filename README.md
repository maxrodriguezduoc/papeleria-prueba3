# PRUEBA PARCIAL III: Papelería The Office

## DESCRIPCIÓN

Un sistema avanzado para gestionar la papelería The Office, evolucionado hacia una Arquitectura de Microservicios. El sistema gestiona entidades relacionadas a los diferentes aspectos del negocio, tales como inventario de productos, locales (junto a sus comunas y regiones), gestión de clientes y un motor transaccional completo para Ventas, Pagos, Tarjetas y Transferencias.

El objetivo del proyecto es agilizar los inventarios, mantener una base de datos distribuida, asegurar la resiliencia mediante comunicación sincrónica (WebClient) y ofrecer una API madura (REST - HATEOAS) para futuras integraciones.

## INTEGRANTES

Ignacio Andrés Acevedo Espinoza - Microservicio Locales y Eureka

Yaritxa González Soto - Microservicio Productos y Clientes

Maximiliano Esteban Rodríguez Conejan - Microservicio Ventas y API Gateway

## TECNOLOGÍAS UTILIZADAS

* Java 21

* Spring Boot 4.x

* Spring Cloud Netflix Eureka (Service Discovery)

* Spring Cloud Gateway (API Gateway)

* Spring HATEOAS (Auto-descubrimiento REST v2)

* Spring WebFlux / WebClient (Comunicación HTTP entre microservicios)

* Spring Data JPA + Hibernate

* JUnit 5 + Mockito (Pruebas Unitarias Mocks & Deep Stubs)

* DataFaker (Generación de datos aleatorios y Testing)

* Laragon / MySQL (Bases de datos físicas independientes)

* Maven

* Lombok

* GitHub

## ARQUITECTURA CSR

Para el proyecto utilizamos el patrón de capas clásico CSR (Controller, Service, Repository) dentro de cada módulo, pero elevado a un Ecosistema de Microservicios:

* Eureka Server: Actúa como el registro central donde todos los microservicios se reportan.

* API Gateway: Único punto de entrada público, encargado de enrutar las peticiones al microservicio correspondiente.

* Microservicios Independientes: Ventas, Producto, Local y Cliente. Cada uno posee su propia conexión a base de datos para garantizar el bajo acoplamiento.

## FUNCIONALIDADES V1

* POST /api/v1/productos → Crear producto

* GET /api/v1/productos → Listar productos
  
* PUT /api/v1/productos/{id} → Actualizar producto
  
* DELETE /api/v1/productos/{id} → Eliminar producto

## FUNCIONALIDADES V2

* POST /api/v2/productos → Crear producto

* GET /api/v2/productos → Listar productos
  
* PUT /api/v2/productos/{id} → Actualizar producto
  
* DELETE /api/v2/productos/{id} → Eliminar producto
  
## INSTRUCCIONES PARA UTILIZAR CÓDIGO

### 1. Clonar Repositorio

git clone https://github.com/maxrodriguezduoc/papeleria-prueba3.git

cd papeleria-prueba3 

### 2. Inicializar base de datos con Laragon

### 3. Ejecutar proyecto con .bat

iniciar-todo.bat

## Ejecutar Tests unitarios:

mvn test

## Ejecutar API en Swagger:

http://localhost:8080/swagger-ui/index.html
