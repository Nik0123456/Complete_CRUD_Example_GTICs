package gtics.l2_20223209_implementacion.Repository;
import gtics.l2_20223209_implementacion.Entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutoRepository extends JpaRepository<Auto, Integer> {
}
