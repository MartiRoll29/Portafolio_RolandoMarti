package pruabTech.Rolando.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pruabTech.Rolando.domain.Rol;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {

    public Optional<Rol> findByRol(String rol);

}
