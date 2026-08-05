package pruabTech.Rolando.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pruabTech.Rolando.domain.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer> {

}
