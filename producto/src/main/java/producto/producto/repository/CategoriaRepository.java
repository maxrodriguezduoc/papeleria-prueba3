package producto.producto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import producto.producto.model.Categoria;

@Repository
public interface CategoriaRepository extends JpaRepository <Categoria, Integer>{

}
