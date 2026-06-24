package com.theoffice.ventas; // Ajusta el paquete según la estructura de tu proyecto

import com.theoffice.ventas.model.Pago;
import com.theoffice.ventas.model.Tarjeta;
import com.theoffice.ventas.model.Transferencia;
import com.theoffice.ventas.model.Venta;
import com.theoffice.ventas.repository.PagoRepository;
import com.theoffice.ventas.repository.TarjetaRepository;
import com.theoffice.ventas.repository.TransferenciaRepository;
import com.theoffice.ventas.repository.VentaRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private TarjetaRepository tarjetaRepository;

    @Autowired
    private TransferenciaRepository transferenciaRepository;

    @Override
    public void run(String... args) throws Exception {

        Faker faker = new Faker();
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            Tarjeta tarjeta = new Tarjeta();
            
            tarjeta.setEsDebito(random.nextBoolean());
            tarjeta.setNumeroTarjeta(faker.number().digits(16));
            tarjeta.setNombreBanco(faker.options().option("Banco Santander", "Banco Estado", "Banco de Chile", "BCI", "Scotiabank"));
            tarjeta.setNombreTitular(faker.name().fullName());
            String mesExpiracion = String.format("%02d", faker.number().numberBetween(1, 13));
            String anioExpiracion = String.valueOf(faker.number().numberBetween(25, 35));
            tarjeta.setFechaExpiracion(mesExpiracion + "/" + anioExpiracion);
            tarjeta.setCvv(faker.number().digits(3));
            tarjeta.setActivo(true);
            tarjetaRepository.save(tarjeta);
        }

        for (int i = 0; i < 5; i++) {
            Transferencia transferencia = new Transferencia();

            transferencia.setBancoOrigen(faker.options().option("Banco Santander", "Banco Estado", "Banco de Chile", "BCI", "Scotiabank"));
            transferencia.setNumeroCuentaOrigen(faker.number().digits(12));
            transferencia.setBancoDestino(faker.options().option("Banco Santander", "Banco Estado", "Banco de Chile", "BCI", "Scotiabank"));
            transferencia.setNumeroCuentaDestino(faker.number().digits(12));
            transferencia.setMonto(faker.number().numberBetween(5000, 250000));
            transferencia.setActivo(true);
            transferenciaRepository.save(transferencia);
        }

        for (int i = 0; i < 20; i++) {
            Venta venta = new Venta();
            
            venta.setCantidad(faker.number().numberBetween(1, 6));
            venta.setTotal_venta(faker.number().numberBetween(1500, 45000));
            venta.setId_producto(faker.number().numberBetween(1, 100));
            venta.setActivo(true);

            ventaRepository.save(venta);
        }

        List<Venta> ventas = ventaRepository.findAll();
        List<Tarjeta> tarjetas = tarjetaRepository.findAll();
        List<Transferencia> transferencias = transferenciaRepository.findAll();
        String[] formasDePago = {"EFECTIVO", "TARJETA_DEBITO", "TARJETA_CREDITO", "TRANSFERENCIA"};

        for (int i = 0; i < ventas.size(); i++) {
            Pago pago = new Pago();
            
            Venta ventaAsignada = ventas.get(i); 
            pago.setVenta(ventaAsignada);
            pago.setMonto(ventaAsignada.getTotal_venta());
            String formaSeleccionada = formasDePago[random.nextInt(formasDePago.length)];
            pago.setFormaPago(formaSeleccionada);
            if (formaSeleccionada.contains("TARJETA") && !tarjetas.isEmpty()) {
                pago.setTarjeta(tarjetas.get(random.nextInt(tarjetas.size())));
            } else if (formaSeleccionada.equals("TRANSFERENCIA") && !transferencias.isEmpty()) {
                pago.setTransferencia(transferencias.get(random.nextInt(transferencias.size())));
            }
            pago.setActivo(true);
            
            pagoRepository.save(pago);
        }
    }
}