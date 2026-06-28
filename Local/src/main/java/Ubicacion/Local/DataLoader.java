package Ubicacion.Local;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import net.datafaker.Faker;

import Ubicacion.Local.model.Cargo;
import Ubicacion.Local.model.Region;
import Ubicacion.Local.model.Comuna;
import Ubicacion.Local.model.Local;
import Ubicacion.Local.model.Colaborador;
import Ubicacion.Local.model.Colaboradores;

import Ubicacion.Local.repository.CargoRepository;
import Ubicacion.Local.repository.RegionRepository;
import Ubicacion.Local.repository.ComunaRepository;
import Ubicacion.Local.repository.LocalRepository;
import Ubicacion.Local.repository.ColaboradorRepository;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ComunaRepository comunaRepository;

    @Autowired
    private LocalRepository localRepository;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Override
    public void run(String... args) {

        Faker faker = new Faker();
        Random random = new Random();

        if (regionRepository.count() > 0) {
            return;
        }

        String[] cargos = {
                "Gerente de Tienda",
                "Subgerente",
                "Cajero",
                "Vendedor",
                "Reponedor",
                "Jefe de Pasillo"
        };

        for (String nombreCargo : cargos) {
            if (!cargoRepository.existsByNombreCargo(nombreCargo)) {
                Cargo cargo = new Cargo();
                cargo.setNombreCargo(nombreCargo);
                cargo.setActivo(true);
                cargoRepository.save(cargo);
            }
        }

        String[] regiones = {
                "Región Metropolitana",
                "Región de Valparaíso",
                "Región del Biobío"
        };

        for (String nombreRegion : regiones) {
            if (!regionRepository.existsByNombreRegion(nombreRegion)) {
                Region region = new Region();
                region.setNombreRegion(nombreRegion);
                region.setActivo(true);
                regionRepository.save(region);
            }
        }

        List<Region> listaRegiones = regionRepository.findAll();

        for (Region region : listaRegiones) {

            if (!region.isActivo()) continue;

            if (region.getNombreRegion().equals("Región Metropolitana")) {
                crearComunas(region, new String[]{"Santiago", "Providencia", "Maipú", "Puente Alto"});
            }

            if (region.getNombreRegion().equals("Región de Valparaíso")) {
                crearComunas(region, new String[]{"Valparaíso", "Viña del Mar", "Quilpué"});
            }

            if (region.getNombreRegion().equals("Región del Biobío")) {
                crearComunas(region, new String[]{"Concepción", "Talcahuano", "Chiguayante"});
            }
        }

        List<Comuna> listaComunas = comunaRepository.findAll();

        if (!listaComunas.isEmpty()) {
            for (int i = 0; i < 10; i++) {
                Local local = new Local();
                local.setNombreLocal("Sucursal " + faker.company().name());
                local.setActivo(true);
                local.setComuna(listaComunas.get(random.nextInt(listaComunas.size())));
                localRepository.save(local);
            }
        }

        List<Cargo> listaCargos = cargoRepository.findAll();
        List<Local> listaLocales = localRepository.findAll();

        if (!listaCargos.isEmpty() && !listaLocales.isEmpty()) {

            for (int i = 0; i < 50; i++) {

                Colaborador colaborador = new Colaborador();
                colaborador.setNombreColaborador(faker.name().fullName());
                colaborador.setActivo(true);
                colaborador.setCargo(listaCargos.get(random.nextInt(listaCargos.size())));

                if (colaborador.getLocales() == null) {
                    colaborador.setLocales(new java.util.ArrayList<>());
                }

                Colaboradores puente = new Colaboradores();
                puente.setColaborador(colaborador);
                puente.setLocal(listaLocales.get(random.nextInt(listaLocales.size())));

                colaborador.getLocales().add(puente);

                colaboradorRepository.save(colaborador);
            }
        }
    }

    private void crearComunas(Region region, String[] nombres) {

        if (region.getIdRegion() == null) return;

        for (String nombre : nombres) {

            boolean existe = comunaRepository
                    .existsByNombreComunaAndRegion_IdRegion(nombre, region.getIdRegion());

            if (!existe) {
                Comuna comuna = new Comuna();
                comuna.setNombreComuna(nombre);
                comuna.setActivo(true);
                comuna.setRegion(region);
                comunaRepository.save(comuna);
            }
        }
    }
}