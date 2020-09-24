package dboard.data;

import dboard.DoodlePost;
import org.springframework.data.repository.CrudRepository;

public interface DoodlePostRepository extends CrudRepository<DoodlePost, Long> {
}
