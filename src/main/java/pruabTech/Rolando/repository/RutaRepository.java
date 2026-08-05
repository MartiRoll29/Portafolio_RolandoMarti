package pruabTech.Rolando.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pruabTech.Rolando.domain.Ruta;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Integer> {

    public List<Ruta> findAllByOrderByRequiereRolAsc();

}
