#!/bin/bash

echo "Iniciando Servidor de Descubrimiento Eureka (Puerto 8761)..."
osascript -e 'tell application "Terminal" to do script "cd \"'"$(pwd)"'/eureka\" && ./mvnw spring-boot:run"'

echo "Esperando 12 segundos a que Eureka se estabilice..."
sleep 12

echo "Iniciando API Gateway..."
osascript -e 'tell application "Terminal" to do script "cd \"'"$(pwd)"'/api-gateway\" && ./mvnw spring-boot:run"'

echo "Iniciando Microservicio Cliente..."
osascript -e 'tell application "Terminal" to do script "cd \"'"$(pwd)"'/cliente\" && ./mvnw spring-boot:run"'

echo "Iniciando Microservicio Local..."
osascript -e 'tell application "Terminal" to do script "cd \"'"$(pwd)"'/Local\" && ./mvnw spring-boot:run"'

echo "Iniciando Microservicio Producto..."
osascript -e 'tell application "Terminal" to do script "cd \"'"$(pwd)"'/producto\" && ./mvnw spring-boot:run"'

echo "Iniciando Microservicio Venta..."
osascript -e 'tell application "Terminal" to do script "cd \"'"$(pwd)"'/Ventas\" && ./mvnw spring-boot:run"'

echo "Ecosistema lanzado. Dashboard disponible en http://localhost:8761"