package producto.producto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import producto.producto.model.TipoProducto;

@Repository
public interface TipoProductoRepository extends JpaRepository <TipoProducto, Integer>{

}
