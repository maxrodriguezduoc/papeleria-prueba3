package producto.producto.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import producto.producto.model.Color;
@Repository
public interface ColorRepository extends JpaRepository <Color, Integer> {

}
