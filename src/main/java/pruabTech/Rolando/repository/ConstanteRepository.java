package pruabTech.Rolando.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pruabTech.Rolando.domain.Constante;

@Repository
public interface ConstanteRepository extends JpaRepository<Constante, Integer> {

    //Consulta derivada que se utiliza para ubicar una constante por su nombre de atributo
    public Optional<Constante> findByAtributo(String atributo);

}
