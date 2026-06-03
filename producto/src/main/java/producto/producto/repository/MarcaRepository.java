package producto.producto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import producto.producto.model.Marca;

@Repository
public interface MarcaRepository extends JpaRepository <Marca, Integer> {

}
