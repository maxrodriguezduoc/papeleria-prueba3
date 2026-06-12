@echo off

echo Iniciando Servidor de Descubrimiento Eureka (Puerto 8761)...
cd eureka
start cmd /k "mvnw spring-boot:run"

echo Esperando 12 segundos a que Eureka se estabilice...
timeout /t 12 /nobreak > null

echo Iniciando API Gateway...
cd ../api-gateway
start cmd /k "mvnw spring-boot:run"

echo Iniciando Microservicio Cliente...
cd ../cliente
start cmd /k "mvnw spring-boot:run"

echo Iniciando Microservicio Local...
cd ../Local
start cmd /k "mvnw spring-boot:run"

echo Iniciando Microservicio Producto...
cd ../producto
start cmd /k "mvnw spring-boot:run"

echo Iniciando Microservicio Ventas...
cd ../Ventas
start cmd /k "mvnw spring-boot:run"

echo Ecosistema lanzado. Dashboard disponible en http://localhost:8761