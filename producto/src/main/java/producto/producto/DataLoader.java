package producto.producto; // Ajusta el paquete según la estructura raíz de tu proyecto

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import net.datafaker.Faker;
import producto.producto.model.Categoria;
import producto.producto.model.Categorias;
import producto.producto.model.Color;
import producto.producto.model.Colores;
import producto.producto.model.Marca;
import producto.producto.model.Marcas;
import producto.producto.model.Producto;
import producto.producto.model.TipoProducto;
import producto.producto.model.TipoProductos;
import producto.producto.repository.CategoriaRepository;
import producto.producto.repository.ColorRepository;
import producto.producto.repository.MarcaRepository;
import producto.producto.repository.ProductoRepository;
import producto.producto.repository.TipoProductoRepository;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private MarcaRepository marcaRepository;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private TipoProductoRepository tipoProductoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public void run(String... args) throws Exception {

        Faker faker = new Faker();
        Random random = new Random();

        // 1. Generar e insertar Categorías Maestras
        for (int i = 0; i < 5; i++) {
            Categoria categoria = new Categoria();
            categoria.setNombre(faker.options().option("Papelería", "Escritorio", "Arte", "Tecnología", "Archivo"));
            categoria.setActivo(true);
            categoriaRepository.save(categoria);
        }

        // 2. Generar e insertar Marcas Maestras
        for (int i = 0; i < 6; i++) {
            Marca marca = new Marca();
            marca.setNombre_marca(faker.options().option("Proarte", "Faber-Castell", "Artel", "Pilot", "Bic", "Torre"));
            marca.setActivo(true);
            marcaRepository.save(marca);
        }

        // 3. Generar e insertar Colores Maestros
        for (int i = 0; i < 8; i++) {
            Color color = new Color();
            color.setNombre_color(faker.color().name());
            color.setActivo(true);
            colorRepository.save(color);
        }

        // 4. Generar e insertar Tipos de Producto Maestros
        for (int i = 0; i < 6; i++) {
            TipoProducto tipoProducto = new TipoProducto();
            tipoProducto.setNombre(faker.options().option("Lápiz", "Cuaderno", "Destacador", "Carpeta", "Goma", "Regla"));
            tipoProducto.setActivo(true);
            tipoProductoRepository.save(tipoProducto);
        }

        // Recuperamos de la BD todos los maestros generados arriba
        List<Categoria> listaCategorias = categoriaRepository.findAll();
        List<Marca> listaMarcas = marcaRepository.findAll();
        List<Color> listaColores = colorRepository.findAll();
        List<TipoProducto> listaTipos = tipoProductoRepository.findAll();

        // 5. Generar e insertar los Productos con sus Listas Puente
        for (int i = 0; i < 30; i++) {
            Producto producto = new Producto();
            
            // Nombre descriptivo aleatorio combinando el tipo y un término comercial
            String tipoAleatorio = listaTipos.get(random.nextInt(listaTipos.size())).getNombre();
            producto.setNombre_producto(tipoAleatorio + " " + faker.commerce().productName());
            
            // Setters exactos de los campos de tu entidad Producto
            producto.setPrecio_producto(faker.number().numberBetween(500, 15000));
            producto.setStock(faker.number().numberBetween(10, 200));
            producto.setActivo(true);

            // Guardamos el producto inicialmente para que genere su ID autoincremental
            producto = productoRepository.save(producto);

            // Lógica de asignación para tus relaciones @OneToMany de entidades puente:
            
            if (!listaCategorias.isEmpty()) {
                Categorias puenteCategoria = new Categorias();
                puenteCategoria.setProducto(producto);
                puenteCategoria.setCategoria(listaCategorias.get(random.nextInt(listaCategorias.size())));
                
                producto.getCategorias().add(puenteCategoria);
            }
            
            if (!listaMarcas.isEmpty()) {
                Marcas puenteMarca = new Marcas();
                puenteMarca.setProducto(producto);
                puenteMarca.setMarca(listaMarcas.get(random.nextInt(listaMarcas.size())));
                
                producto.getMarcas().add(puenteMarca);
            }
            
            if (!listaColores.isEmpty()) {
                Colores puenteColor = new Colores();
                puenteColor.setProducto(producto);
                puenteColor.setColor(listaColores.get(random.nextInt(listaColores.size())));
                
                producto.getColores().add(puenteColor);
            }
            
            if (!listaTipos.isEmpty()) {
                TipoProductos puenteTipo = new TipoProductos();
                puenteTipo.setProducto(producto);
                puenteTipo.setTipoProducto(listaTipos.get(random.nextInt(listaTipos.size())));
                
                producto.getTiposProducto().add(puenteTipo);
            }

            // Guardado final en cascada actualizando el producto con sus respectivas colecciones puente
            productoRepository.save(producto);
        }
    }
}