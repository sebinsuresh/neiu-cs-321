package dboard.data;

import dboard.DoodlePost;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DoodlePostRepository extends CrudRepository<DoodlePost, Long> {
    // Returns Doodle posts sorted by "latest post first"
    List<DoodlePost> findAllByOrderByPostedAtDesc();
}
