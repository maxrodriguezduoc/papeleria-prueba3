package Ubicacion.Local;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import Ubicacion.Local.model.Cargo;
import Ubicacion.Local.model.Region;
import Ubicacion.Local.model.Comuna;
import Ubicacion.Local.model.Local;
import Ubicacion.Local.model.Colaborador;
import Ubicacion.Local.model.Colaboradores;
import Ubicacion.Local.repository.CargoRepository;
import Ubicacion.Local.repository.RegionRepository;
import net.datafaker.Faker;
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
        
        String[] cargos = {
            "Gerente de Tienda",
            "Subgerente",
            "Cajero",
            "Vendedor",
            "Reponedor",
            "Jefe de Pasillo"
        };
        for (String nombreCargo : cargos){
            Cargo cargo = new Cargo();
            cargo.setNombreCargo(nombreCargo);
            cargo.setActivo(true);
            cargoRepository.save(cargo);
        }

        String[] regiones = {
            "Región Metropolitana",
            "Región de Valparaíso",
            "Región del Biobío"
        };

        for (String nombreRegion : regiones) {
            Region region = new Region();
            region.setNombreRegion(nombreRegion);
            region.setActivo(true);
            regionRepository.save(region);
        }

        List<Region> listaRegiones = regionRepository.findAll();
        for (Region region : listaRegiones) {

            if (region.getNombreRegion().equals("Región Metropolitana")) {
                String[] comunasRM = {"Santiago", "Providencia", "Maipú", "Puente Alto"};

                for (String nombre : comunasRM) {
                    Comuna comuna = new Comuna();
                    comuna.setNombreComuna(nombre);
                    comuna.setActivo(true);
                    comuna.setRegion(region);
                    comunaRepository.save(comuna);
                }
            }

            if (region.getNombreRegion().equals("Región de Valparaíso")) {
                String[] comunasValpo = {"Valparaíso", "Viña del Mar", "Quilpué"};

                for (String nombre : comunasValpo) {
                    Comuna comuna = new Comuna();
                    comuna.setNombreComuna(nombre);
                    comuna.setActivo(true);
                    comuna.setRegion(region);
                    comunaRepository.save(comuna);
                }
            }

            if (region.getNombreRegion().equals("Región del Biobío")) {
                String[] comunasBio = {"Concepción", "Talcahuano", "Chiguayante"};

                for (String nombre : comunasBio) {
                    Comuna comuna = new Comuna();
                    comuna.setNombreComuna(nombre);
                    comuna.setActivo(true);
                    comuna.setRegion(region);
                    comunaRepository.save(comuna);
                }
            }
        }

        List<Comuna> listaComunas = comunaRepository.findAll();

        for (int i = 0; i < 10; i++) {
            Local local = new Local();
            local.setNombreLocal("Sucursal " + faker.company().name());
            local.setActivo(true);
            local.setComuna(listaComunas.get(random.nextInt(listaComunas.size())));
            localRepository.save(local);
        }

        List<Cargo> listaCargos = cargoRepository.findAll();
        List<Local> listaLocales = localRepository.findAll();

        for (int i = 0; i < 50; i++) {
            Colaborador colaborador = new Colaborador();
            colaborador.setNombreColaborador(faker.name().fullName());
            colaborador.setActivo(true);
            colaborador.setCargo(listaCargos.get(random.nextInt(listaCargos.size())));
            
            Colaboradores puente = new Colaboradores();
            puente.setColaborador(colaborador);
            puente.setLocal(listaLocales.get(random.nextInt(listaLocales.size())));

            colaborador.getLocales().add(puente);
            
            colaboradorRepository.save(colaborador);
        }
    }
}