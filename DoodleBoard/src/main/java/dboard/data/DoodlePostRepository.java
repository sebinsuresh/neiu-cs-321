package dboard.data;

import dboard.DoodlePost;
import dboard.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DoodlePostRepository extends CrudRepository<DoodlePost, Long> {
    // Returns Doodle posts sorted by "latest post first", with the user id that matches given user
    List<DoodlePost> findAllByUserOrderByPostedAtDesc(User user, Pageable pageable);
    // List<DoodlePost> findAllByOrderByPostedAtDesc();
}
