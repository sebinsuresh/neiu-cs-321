package dboard.data;

import dboard.Doodle;
import org.springframework.data.repository.CrudRepository;

public interface DoodleRepository extends CrudRepository<Doodle, Long> {
}
